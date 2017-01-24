package com.cornez.studentdiscountsapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;
    // A request object to store parameters for requests to the FusedLocationProviderApi.
    private LocationRequest mLocationRequest;
    // The desired interval for location updates. Inexact. Updates may be more or less frequent.
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    // The fastest rate for active location updates. Exact. Updates will never be more frequent
    // than this value.
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located.
    private Location mCurrentLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        // Do other setup activities here too.
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            getDeviceLocation();
        }
        updateMarkers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
            @Override
            public View getInfoWindow(Marker arg0){
                return null;
            }
            @Override
            public View getInfoContents(Marker marker){
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());
                return infoWindow;
            }
        });

        if(mCameraPosition !=null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition((mCameraPosition)));
        } else if (mCurrentLocation != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mCurrentLocation.getLatitude(),
                            mCurrentLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Usin deafaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation,DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint){
        getDeviceLocation();
        if(mLocationPermissionGranted) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mMap!=null){
            outState.putParcelable(KEY_CAMERA_POSITION,mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mCurrentLocation);
            super.onSaveInstanceState(outState);
        }
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        createLocationRequest();
    }

    private void getDeviceLocation(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permission[],
                                          @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch(requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                //if request is cancelled, the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if(mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mCurrentLocation = null;
        }
    }

    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location){
        mCurrentLocation = location;
        updateMarkers();
    }

    private void updateMarkers() {
        if(mMap == null) {
            return;
        }

        if(mLocationPermissionGranted) {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                    for(PlaceLikelihood placeLikelihood : likelyPlaces){
                        String attributions = (String) placeLikelihood.getPlace().getAttributions();
                        String snippet = (String) placeLikelihood.getPlace().getAddress();
                        if (attributions != null) {
                            snippet = snippet + "\n" + attributions;
                        }

                        mMap.addMarker(new MarkerOptions()
                                .position(placeLikelihood.getPlace().getLatLng())
                                .title((String) placeLikelihood.getPlace().getName())
                                .snippet(snippet));
                    }
                    likelyPlaces.release();
                }
            });
        } else{
            mMap.addMarker(new MarkerOptions()
                    .position(mDefaultLocation)
                    .title(getString(R.string.default_info_title))
                    .snippet(getString(R.string.default_info_snippet)));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
