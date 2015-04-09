package com.example.hector.multicinesbectar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsCinesActivity extends Fragment {

    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;
    MapHelper mapHelper;
    static final LatLng Palafox = new LatLng(41.651, -0.88267717);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.activity_maps, container, false);

        MapsInitializer.initialize(getActivity());

        mMapView = (MapView) inflatedView.findViewById(R.id.map);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded(inflatedView);
        mapHelper = new MapHelper(mMap);
        //Set the map type
        mapHelper.setMapType(MapTypes.NORMAL);

        //Set if you want your current location to be highlighted
        mapHelper.setCurrentLocation(true);

        //To enable the zoom controls (+/- buttons)
        mapHelper.setZoomControlsEnabled(true);

        //To enable gestures
        mapHelper.setZoomGesturesEnabled(true);

        //To enable compass
        mapHelper.setCompassEnabled(true);

        //Set to have a my location button which on clicked moves to your current location
        mapHelper.setMyLocationButtonEnabled(true);

        //To enable rotation in your map
        mapHelper.setRotateGesturesEnabled(true);



        return inflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    private void setUpMapIfNeeded(View inflatedView) {
        if (mMap == null) {
            mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(15));
        // Zoom in, animating the camera.


        mMap.addMarker(new MarkerOptions().position(new LatLng(41.651, -0.88267717)).title("Cines Palafox").snippet("Multicine cn estrenos y cine clásico"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(41.6220213, -0.8803059)).title("Cines Cinesa\nPuerto Venecia").snippet("Cinesa"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(41.651, -0.88267717)).zoom(12)
                .build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        //k.setCurrentLocation(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
}
