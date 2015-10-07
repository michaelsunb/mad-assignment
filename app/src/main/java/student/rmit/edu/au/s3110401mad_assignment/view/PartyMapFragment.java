package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.PartyEditActivity;
import student.rmit.edu.au.s3110401mad_assignment.controller.PartyListActivity;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyStruct;
import student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility.PartyMemoryManagementClient;

public class PartyMapFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    public static final Double DEFAULT_LAT = -37.8070901;
    public static final Double DEFAULT_LONG = 144.9649695;
    public static final float MAP_ZOOM_PADDING = 12;

    private GoogleMap googleMap;

    private List<MarkerOptions> markers = new ArrayList<>();
    private List<Party> parties;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        try {

            View rootView = inflater.inflate(R.layout.fragment_party_map, container, false);
            rootView.findViewById(R.id.main_party_list_button).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goPartyListActivity();
                        }
                    });
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setOnMarkerClickListener(this);

            parties = PartyModel.getSingleton().getAllParties();
            setMap();

            return rootView;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setMap() {
        this.markers = new ArrayList<>();
        if(parties != null && parties.size() > 0) {
            for (Party party : parties) {
                this.markers.add(setPartyToMarker(party));
            }
        }
        onMapReady(googleMap);
    }

    private void goPartyListActivity() {
        if(parties != null && parties.size() == 0
                || parties == null) {
            Toast.makeText(
                    getActivity(),"No parties happening!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(getActivity(), PartyListActivity.class);
        startActivity(intent);
    }

    public void initialMap(GoogleMap map) {
        LatLng RMITUniversity = new LatLng(DEFAULT_LAT, DEFAULT_LONG);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(RMITUniversity, MAP_ZOOM_PADDING));
    }

    public void onMapReady(GoogleMap map) {
        if(map == null) return;
        if(markers.size() == 0) {
            initialMap(map);
            return;
        }
        map.clear();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        /**
         * Dynamically build markers to set
         * for map zoom
         */
        for (MarkerOptions location : markers) builder.include(location.getPosition());
        final LatLngBounds bounds = builder.build();

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
                googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), MAP_ZOOM_PADDING));
                googleMap.setOnCameraChangeListener(null);
            }
        });

        for (MarkerOptions marker : markers) {
            googleMap.addMarker(marker);
        }
    }

    public void setParties(PartyMemoryManagementClient partyTask) {
        try {
            parties = partyTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private MarkerOptions setPartyToMarker(Party party) {
        return new MarkerOptions()
                .position(new LatLng(
                        party.getLocation()[PartyStruct.LONGITUDE],
                        party.getLocation()[PartyStruct.LATITUDE]
                ))
                .title(party.getMovieTitle())
                .snippet(party.getVenue());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int index = 0;
        for(MarkerOptions markerOptions : markers) {
            if (marker.getTitle().equals(markerOptions.getTitle()) &&
                    marker.getSnippet().equals(markerOptions.getSnippet()) &&
                    marker.getPosition().latitude == markerOptions.getPosition().latitude &&
                    marker.getPosition().longitude == markerOptions.getPosition().longitude) {
                try {
                    Intent intent = new Intent(getActivity(), PartyEditActivity.class);
                    intent.putExtra("party_id", parties.get(index).getId());
                    startActivityForResult(intent, 1);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            index++;
        }
        return false;
    }
}
