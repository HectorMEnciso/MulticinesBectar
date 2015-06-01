package com.example.hector.multicinesbectar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsCinesActivity extends Fragment {

    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;
    private MapHelper mapHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.activity_maps, container, false);

        MapsInitializer.initialize(getActivity());

        mMapView = (MapView) inflatedView.findViewById(R.id.map);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded(inflatedView);
        mapHelper = new MapHelper(mMap);

        mapHelper.setMapType(MapTypes.NORMAL);//define el tipo de mapa


        mapHelper.setCurrentLocation(true);//Muestra mi posicion actual


        mapHelper.setZoomControlsEnabled(true);//Habilita los botones zoom


        mapHelper.setZoomGesturesEnabled(true);//Habilita gestos.


        mapHelper.setCompassEnabled(true);//Habilita la brújula


        mapHelper.setMyLocationButtonEnabled(true);//Nos lleva a nuestra posicion actual


        mapHelper.setRotateGesturesEnabled(true);//Permite rotacion con gestos.

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
        /*
        * Mis marcadores para los cines.
        * */
        mMap.addMarker(new MarkerOptions().position(new LatLng(41.651, -0.88267717)).title("CINE PALAFOX").snippet("Multicine con estrenos y cine clásico"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(41.6220213, -0.8803059)).title("CINESA \nPUERTO VENCECIA 3D").snippet("Cines Puerto Venecia"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(41.640309, -0.909454)).title("CINES ARAGONIA").snippet("Aragonia"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(41.650331, -0.88522)).title("CINES CERVANTES").snippet("Órpera y proyecciones cinematográficas"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(41.6708, -0.890254)).title("CINESA GRANCASA").snippet("Cadena de cines multisala de estreno"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(41.646483, -0.885755)).title("CINES ELISEOS").snippet("Estrenos de una clásica de 1944"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(41.634345, -0.985562)).title("YELMO CINES PLAZA IMPERIAL").snippet("Cines multisala de estreno con eventos"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(41.651, -0.88267717)).zoom(12)
                .build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
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
