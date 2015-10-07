package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.Activity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.MovieDetailActivity;
import student.rmit.edu.au.s3110401mad_assignment.controller.PartyListActivity;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyStruct;
import student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility.PartyMemoryManagementClient;

public class PartyMapFragment extends Fragment implements OnMapReadyCallback {
    public static final Double DEFAULT_LAT = -37.807085;
    public static final Double DEFAULT_LONG = 144.964282;
    public static final int MAP_ZOOM_PADDING = 25;

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

            if(parties != null && parties.size() > 0) {
                this.markers = new ArrayList<>();
                for (Party party : parties) {
                    this.markers.add(setPartyToMarker(party));
                }
            }
            onMapReady(googleMap);

            return rootView;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public void onMapReady(final GoogleMap map) {
        if(map == null) return;
        if(markers.size() <= 0) {
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

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, MAP_ZOOM_PADDING));
                map.setOnCameraChangeListener(null);
            }
        });

        // Dynamically add markers
        final Map<String,Marker> markerMap = new HashMap<>();
        for (MarkerOptions marker : markers) {
            Marker m = map.addMarker(marker);
            markerMap.put(m.getId(),m);
        }

        if(markerMap.size() <= 0) return;

        final Activity context = getActivity();
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra(getString(R.string.movie_id), marker.getId());
                startActivityForResult(intent, 1);
                return true;
            }
        });
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
                .title(party.getImDB())
                .snippet(party.getVenue());
    }
}
