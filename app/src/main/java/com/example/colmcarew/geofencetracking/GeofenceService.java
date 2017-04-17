package com.example.colmcarew.geofencetracking;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by colmcarew on 17/04/2017.
 */

public class GeofenceService extends IntentService {
    private static final String TAG = "GeoSer";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GeofenceService(String name) {
        super(name);
    }
    public GeofenceService() {
        super(TAG);  // use TAG to name the IntentService worker thread
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if(event.hasError()) {
            //Handle it
        } else {
            int transition = event.getGeofenceTransition();
            List<Geofence> geofenceList = event.getTriggeringGeofences();
            Geofence geofence = geofenceList.get(0);
            String requedId = geofence.getRequestId();
            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER){
                Log.w(TAG, "Entering Geofence - " + requedId);
            } else  if(transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.w(TAG, "Exiting Geofence - " + requedId);
            }
        }
    }
}
