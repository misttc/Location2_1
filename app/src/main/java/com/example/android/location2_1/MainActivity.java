package com.example.android.location2_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import android.location.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity
        implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    protected final String LOG_TAG = "Location Services 2-1";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    protected TextView LaText;
    protected TextView LoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LaText = (TextView) findViewById(R.id.LaText);
        LoText = (TextView) findViewById(R.id.LoText);
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //  Connect the Client
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnect the Client
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected (Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            LaText.setText(String.valueOf(mLastLocation.getLatitude()));
            LoText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionFailed (ConnectionResult result) {
        Log.i(LOG_TAG, "Connection failed: " + result.getErrorCode());
    }

    public void onDisconnected(){
        Log.i(LOG_TAG, "Disconnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "GoogleApiClient conection has been suspended");
    }

    @Override
    public void onLocationChanged(Location location){
        Log.i(LOG_TAG, location.toString());

        LaText.setText(String.valueOf(mLastLocation.getLatitude()));
        LoText.setText(String.valueOf(mLastLocation.getLongitude()));
    }
}
