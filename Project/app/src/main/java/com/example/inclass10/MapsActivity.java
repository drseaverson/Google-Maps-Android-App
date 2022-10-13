package com.example.inclass10;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.inclass10.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String TAG = "demo";

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        double largeLat, smallLat, largeLong, smallLong;
        Gson gson = new Gson();

        Path path = gson.fromJson(readData(), Path.class);
        //Log.d(TAG, "onMapReady: " + path);
        PolylineOptions polylineOptions = new PolylineOptions();
        //sets information for where to restrict page to based on largest/smallest long/lat
        largeLat = path.points.get(0).latitude;
        smallLat = path.points.get(0).latitude;
        smallLong = path.points.get(0).longitude;
        largeLong = path.points.get(0).longitude;
        for (Point point : path.points) {
            polylineOptions.add(new LatLng(point.latitude, point.longitude));
            if (point.latitude > largeLat) {
                largeLat = point.latitude;
            } else if (point.latitude < smallLat) {
                smallLat = point.latitude;
            }
            if (point.longitude > largeLong) {
                largeLat = point.longitude;
            } else if (point.longitude < smallLong) {
                smallLong = point.longitude;
            }
        }
        polylineOptions.color(Color.BLUE);

        mMap = googleMap;
        Polyline polyline = mMap.addPolyline(polylineOptions);
        mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(30.0f);

        LatLngBounds bounds = new LatLngBounds(
                new LatLng(smallLat, smallLong),
                new LatLng(largeLat, largeLong)
        );
        Log.d(TAG, "onMapReady: " + smallLat);
        Log.d(TAG, "onMapReady: " + smallLong);

        // Add a marker in Sydney and move the camera
        LatLng start = new LatLng(path.points.get(0).latitude, path.points.get(0).longitude);
        mMap.addMarker(new MarkerOptions().position(start).title(path.title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        LatLng end = new LatLng(path.points.get(path.points.size() - 1).latitude, path.points.get(path.points.size() - 1).longitude);
        mMap.addMarker(new MarkerOptions().position(end).title(path.title));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10));
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
            }
        });
    }

    public String readData() {
        String json = null;
        try {
            InputStream inputStream = getApplicationContext().getAssets().open("trip.json");

            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, "UTF-8");
            //Log.d(TAG, "readData: " + json);

        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        //Log.d(TAG, "readData: " + json);
        return json;
    }
}