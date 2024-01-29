package com.example.location_det;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback1;
    private LocationCallback locationCallback2;
    private TextView latitudeTextView1;
    private TextView longitudeTextView1;
    private TextView latitudeTextView2;
    private TextView longitudeTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        latitudeTextView1 = findViewById(R.id.latitudeTextView1);
        longitudeTextView1 = findViewById(R.id.longitudeTextView1);
        latitudeTextView2 = findViewById(R.id.latitudeTextView2);
        longitudeTextView2 = findViewById(R.id.longitudeTextView2);

        if (checkLocationPermission()) {
            // Permission already granted, proceed to get location
            // getLocation1();  // Commented to avoid automatic location updates on app start
        } else {
            // Request permission
            requestLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }

    private void getLocation1() {
        if (checkLocationPermission()) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            locationCallback1 = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }

                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Update the TextViews with the new latitude and longitude
                        updateLocationTextViews1(latitude, longitude);
                    }
                }
            };

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback1, null);
        } else {
            // Permission not granted, handle accordingly (e.g., show a message)
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLocationTextViews1(double latitude, double longitude) {
        latitudeTextView1.setText("Latitude: " + latitude);
        longitudeTextView1.setText("Longitude: " + longitude);
    }

    public void onGetLocationClick1(View view) {
        getLocation1();
    }

    private void getLocation2() {
        if (checkLocationPermission()) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            locationCallback2 = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }

                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Update the TextViews with the new latitude and longitude
                        updateLocationTextViews2(latitude, longitude);
                    }
                }
            };

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback2, null);
        } else {
            // Permission not granted, handle accordingly (e.g., show a message)
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLocationTextViews2(double latitude, double longitude) {
        latitudeTextView2.setText("Latitude: " + latitude);
        longitudeTextView2.setText("Longitude: " + longitude);
    }

    public void onGetLocationClick2(View view) {
        getLocation2();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationClient != null) {
            if (locationCallback1 != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback1);
            }
            if (locationCallback2 != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback2);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to get location
                // getLocation1();  // Commented to avoid automatic location updates on permission grant
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
