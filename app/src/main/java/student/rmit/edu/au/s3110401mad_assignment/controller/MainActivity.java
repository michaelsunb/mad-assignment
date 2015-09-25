package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private MapFragment mapFragment;

    private List<MarkerOptions> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (googleMap == null) {
                getMapFragment().getMapAsync(this);
                googleMap = getMapFragment().getMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //setMarkers(new RestJobsModel().queryAllJobs());
    }

    public void setMarkers(List<MarkerOptions> locations) {
        this.markers = new ArrayList<>();
        this.markers = locations;

        onMapReady(googleMap);
    }


    @Override
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

        final MainActivity context = this;
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

    private MapFragment getMapFragment() {
        if(mapFragment == null) {
            mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map_content_frame);
        }
        return mapFragment;
    }
}