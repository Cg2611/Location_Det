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
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView latitudeTextView1;
    private TextView longitudeTextView1;
    private TextView latitudeTextView2;
    private TextView longitudeTextView2;
    private TextView distanceTextView;
    private double lat1, lon1, lat2, lon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        latitudeTextView1 = findViewById(R.id.latitudeTextView1);
        longitudeTextView1 = findViewById(R.id.longitudeTextView1);
        latitudeTextView2 = findViewById(R.id.latitudeTextView2);
        longitudeTextView2 = findViewById(R.id.longitudeTextView2);
        distanceTextView = findViewById(R.id.distanceTextView);

        if (checkLocationPermission()) {
            // Permission already granted, proceed to get location
            getLastKnownLocation();
        } else {
            // Request permission
            requestLocationPermission();
        }
    }

    private void getLastKnownLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            updateLocationTextViews1(location.getLatitude(), location.getLongitude());
                        }
                    });
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dlon = lon2Rad - lon1Rad;
        double dlat = lat2Rad - lat1Rad;
        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double R = 6371;

        return R * c;
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

    public void onGetLocationClick1(View view) {
        getLastKnownLocation();
    }

    public void onGetLocationClick2(View view) {
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            updateLocationTextViews2(location.getLatitude(), location.getLongitude());
                            calculateAndDisplayDistance();
                        }
                    });
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLocationTextViews1(double latitude, double longitude) {
        latitudeTextView1.setText("Latitude: " + latitude);
        longitudeTextView1.setText("Longitude: " + longitude);

        lat1 = latitude;
        lon1 = longitude;
    }

    private void updateLocationTextViews2(double latitude, double longitude) {
        latitudeTextView2.setText("Latitude: " + latitude);
        longitudeTextView2.setText("Longitude: " + longitude);

        lat2 = latitude;
        lon2 = longitude;
    }

    private void calculateAndDisplayDistance() {
        if (lat1 != 0 && lon1 != 0 && lat2 != 0 && lon2 != 0) {
            double distance = calculateDistance(lat1, lon1, lat2, lon2);
            distanceTextView.setText("Distance: " + distance + " km");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();  // Fetch location after permission is granted
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
