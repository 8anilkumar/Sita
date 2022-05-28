package com.eminence.sitasrm.Utils;

import static com.google.android.gms.common.api.CommonStatusCodes.RESOLUTION_REQUIRED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class GpsUtils {
    private Context context;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationManager locationManager;
    private LocationRequest locationRequest;

    public GpsUtils(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mSettingsClient = LocationServices.getSettingsClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10L * 1000L);
        locationRequest.setFastestInterval(2L * 1000L);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();
        builder.setAlwaysShow(true);
    }

    public void turnGPSOn(final OnGpsListener onGpsListener) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (onGpsListener != null) {
                onGpsListener.gpsStatus(true);
            }
        } else {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("GPS Permission");
            alertDialog.setMessage("Please allow us to access GPS Setting to give you better experience to our app.\nWe need location for the following things and mentioned here.\nTo deliver product at your location   ");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mSettingsClient
                            .checkLocationSettings(mLocationSettingsRequest)
                            .addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                                @SuppressLint("MissingPermission")
                                @Override
                                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                                    if (onGpsListener != null) {
                                        onGpsListener.gpsStatus(true);
                                    }
                                }
                            })
                            .addOnFailureListener((Activity) context, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    int statusCode = ((ApiException) e).getStatusCode();
                                    switch (statusCode) {
                                        case RESOLUTION_REQUIRED:
                                            try {
                                                ResolvableApiException rae = (ResolvableApiException) e;
                                                rae.startResolutionForResult((Activity) context, 1001);
                                            } catch (IntentSender.SendIntentException sie) {
                                                Log.i("TAG", "PendingIntent unable to execute request.");
                                            }
                                            break;
                                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                                    "fixed here. Fix in Settings.";
                                            Log.e("TAG", errorMessage);
                                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                }
            });
            alertDialog.setNegativeButton("dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });

            alertDialog.show();
        }
    }

    public interface OnGpsListener {
        void gpsStatus(boolean isGPSEnable);
    }
}