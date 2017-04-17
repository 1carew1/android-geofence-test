package com.example.colmcarew.geofencetracking;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button startLocationMonitoring;
    private Button startGeofenceMonitoring;
    private Button stopGeofenceMonitoring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startLocationMonitoring = (Button) findViewById(R.id.startLocMonitoring);
        startGeofenceMonitoring = (Button) findViewById(R.id.startGeoFMon);
        stopGeofenceMonitoring = (Button) findViewById(R.id.stopGeofenMon);

        startLocationMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationMonitoring();
            }
        });
        startGeofenceMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGeofencMonitroing();
            }
        });
        stopGeofenceMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGeofenceMonitoring();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1234);
        }

    }

    private void startLocationMonitoring() {
        Log.w(TAG, "Starting Location Monitoring");

    }

    private void startGeofencMonitroing() {
        Log.w(TAG, "Starting Geofence Monitoring");

    }

    private void stopGeofenceMonitoring() {
        Log.w(TAG, "Stopping Geofence Monitoring");

    }
}
