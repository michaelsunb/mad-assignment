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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.MovieDetailActivity;
import student.rmit.edu.au.s3110401mad_assignment.controller.PartyListActivity;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;

public class PartyMapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private MapFragment mapFragment;

    private List<MarkerOptions> markers = new ArrayList<>();
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_party_map, container, false);

        rootView.findViewById(R.id.main_party_list_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goPartyListActivity();
                    }
                });

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            onMapReady(googleMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //setMarkers(new RestJobsModel().queryAllJobs());

        return rootView;
    }

    private void goPartyListActivity() {
        if(PartyModel.getSingleton().getAllParties().size() <= 0) {
            Toast.makeText(
                    getActivity(),"No parties happening!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(getActivity(), PartyListActivity.class);
        startActivity(intent);
    }

    public void setMarkers(List<MarkerOptions> locations) {
        this.markers = new ArrayList<>();
        this.markers = locations;

        onMapReady(googleMap);
    }

    public void onMapReady(final GoogleMap map) {
        if(markers.size() <= 0) return;
        map.clear();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        /**
         * Dynamically build markers to set
         * for map zoom
         */
        for (MarkerOptions location : markers) builder.include(location.getPosition());
        final LatLngBounds bounds = builder.build();

        //map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
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

    @Override
    public void onDestroyView() {

//        MapFragment f = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//
//        if (f != null) {
//            try {
//                getFragmentManager().beginTransaction().remove(f).commit();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }

        super.onDestroyView();
    }
}
