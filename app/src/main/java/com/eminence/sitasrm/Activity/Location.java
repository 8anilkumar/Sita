package com.eminence.sitasrm.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.eminence.sitasrm.MainActivity;
import com.eminence.sitasrm.R;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Location extends AppCompatActivity {

    private static int updateInterval = 3000;
    private static int fatestinterval = 3000;
    private static int displacement = 0;

    Context context;
    Button enableLocationBtn;
    IOSDialog iosDialog;
    String userInfo;
    LocationManager locationManager;
    FirebaseStorage storage;
    StorageReference storageReference;
    LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        context = Location.this;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        iosDialog = new IOSDialog.Builder(context)
                .setCancelable(false)
                .setSpinnerClockwise(false)
                .setMessageContentGravity(Gravity.END)
                .build();

        enableLocationBtn = findViewById(R.id.enable_location_btn);

        enableLocationBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                gpsstatus();

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void gpsstatus() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsStatus) {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 2);
        } else {
            getCurrentlocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentlocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            iosDialog.cancel();
            getLocationPermission();
            return;
        }

        createLocationRequest();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationPermission() {

        try {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        } catch (Exception b) {
            b.printStackTrace();
        }

    }


    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(updateInterval);
        mLocationRequest.setFastestInterval(fatestinterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(displacement);

        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (android.location.Location location : locationResult.getLocations()) {
                    if (location != null) {

                        goNext(location);
                        break;
                    }
                }

            }
        };

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback
                , Looper.myLooper());

    }

    public void goNext(android.location.Location location) {

        if (location != null) {

            location.getLatitude();
            location.getLongitude();
            String locationString = "" + location.getLatitude() + ", " + location.getLongitude();
            stopLocationUpdates();
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("goto", "home");
            startActivity(intent);
            finish();


        }

    }

     protected void stopLocationUpdates() {
        if (mFusedLocationClient != null && locationCallback != null)
            mFusedLocationClient.removeLocationUpdates(locationCallback);

    }

     @Override
    public void onDestroy() {
        stopLocationUpdates();
        super.onDestroy();
    }
}