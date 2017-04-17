package com.example.colmcarew.geofencetracking;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button startLocationMonitoring;
    private Button startGeofenceMonitoring;
    private Button stopGeofenceMonitoring;

    private GoogleApiClient googleApiClient = null;

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

        //Setup the Api client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.w(TAG, "connected to GoogleApi client");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.w(TAG, "suspended to GoogleApi client");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.w(TAG, "failed to GoogleApi client");

                    }
                })
                .build();
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        Log.w(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        Log.w(TAG, "onStop");
        super.onStop();
    }

    private void startLocationMonitoring() {
        Log.w(TAG, "Starting Location Monitoring");
        try {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(2000)
                    .setFastestInterval(1000)
                    .setSmallestDisplacement(0)
                    .setMaxWaitTime(2000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Do Nothing
            } else {
                Log.w(TAG, "Starting request updates");
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                        locationRequest, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Log.w(TAG, "Location update" + location.getLatitude() + "," + location.getLongitude());
                            }
                        });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
        }

    }

    private void startGeofencMonitroing() {
        Log.w(TAG, "Starting Geofence Monitoring");
        try {
            Geofence geofence = new Geofence.Builder()
                    .setRequestId("GEO-1")
                    .setCircularRegion(33, -84, 100)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setNotificationResponsiveness(1000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();

            GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(geofence)
                    .build();

            Intent intent = new Intent(this, GeofenceService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (!googleApiClient.isConnected()) {
                Log.w(TAG, "Google API Not Connected");
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // do nothing
                } else {
                    LocationServices.GeofencingApi.addGeofences(googleApiClient, geofencingRequest, pendingIntent)
                            .setResultCallback(new ResultCallback<Status>() {
                                @Override
                                public void onResult(@NonNull Status status) {
                                    if (status.isSuccess()) {
                                        Log.w(TAG, "Successfully Added Geofence");
                                    } else {
                                        Log.w(TAG, "Error added geofence");
                                    }
                                }
                            });
                }

            }
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
        }

    }

    private void stopGeofenceMonitoring() {
        Log.w(TAG, "Stopping Geofence Monitoring");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
