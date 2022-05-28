package com.eminence.sitasrm.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminence.sitasrm.Activity.ImagePickerActivity;
import com.eminence.sitasrm.Activity.LanguageActivity;
import com.eminence.sitasrm.Activity.Profile.AboutActivity;
import com.eminence.sitasrm.Activity.Profile.Address;
import com.eminence.sitasrm.Activity.Profile.Contactus;
import com.eminence.sitasrm.Activity.Profile.EditProfile;
import com.eminence.sitasrm.Activity.Profile.Feedback;
import com.eminence.sitasrm.Activity.Profile.PrivacyPolicies;
import com.eminence.sitasrm.Activity.Profile.SendQuery;
import com.eminence.sitasrm.Activity.Profile.Termcondition;
import com.eminence.sitasrm.Activity.RetailerSignup;
import com.eminence.sitasrm.Activity.Scanner;
import com.eminence.sitasrm.Activity.UpdateRetailerProfile;
import com.eminence.sitasrm.Activity.WhoeareYou;
import com.eminence.sitasrm.Models.CartResponse;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.app.Activity.RESULT_OK;
import static com.eminence.sitasrm.Fragments.CartFragment.subStatus;
import static com.eminence.sitasrm.Utils.Baseurl.baseurl;
import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

public class ProfileFragment extends Fragment {

    LinearLayout addressLayout, languageLayout, feedbackLayout, contactLayout, sendqueryLayout,
            aboutusLayout, termAndConditionLayout, privacyLayout, editLayout;
    RelativeLayout logoutLayout,userProfileLayout;
    ImageView img_profilephoto;
    TextView txt_name, txt_number, txt_emial,langugetext;
    String convertedimage;
    ProgressBar mainProgressBar;
    static DatabaseHandler databaseHandler;
    String userType = "";
    public static final int REQUEST_IMAGE = 100;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        editLayout = view.findViewById(R.id.editLayout);
        addressLayout = view.findViewById(R.id.addressLayout);
        languageLayout = view.findViewById(R.id.languageLayout);
        feedbackLayout = view.findViewById(R.id.feedbackLayout);
        contactLayout = view.findViewById(R.id.contactLayout);
        sendqueryLayout = view.findViewById(R.id.sendqueryLayout);
        aboutusLayout = view.findViewById(R.id.aboutusLayout);
        termAndConditionLayout = view.findViewById(R.id.termAndConditionLayout);
        privacyLayout = view.findViewById(R.id.privacyLayout);
        logoutLayout = view.findViewById(R.id.logoutLayout);
        userProfileLayout = view.findViewById(R.id.userProfileLayout);
        langugetext = view.findViewById(R.id.langugetext);
        img_profilephoto = view.findViewById(R.id.img_profilephoto);
        txt_name = view.findViewById(R.id.txt_name);
        txt_number = view.findViewById(R.id.txt_number);
        txt_emial = view.findViewById(R.id.txt_emial);

        mainProgressBar = view.findViewById(R.id.mainProgressBar);

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subStatus = "0";
                SharedPreferences preferences = getActivity().getSharedPreferences("YourCustomNamedPreference", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(), WhoeareYou.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        setUpDB();

        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String language = yourPrefrence.getData("language");
        if (language.equalsIgnoreCase("en")||language.equalsIgnoreCase("")) {
            langugetext.setText("English");
        } else if(language.equalsIgnoreCase("hi")){
            langugetext.setText("हिंदी");
        } else if(language.equalsIgnoreCase("pa")){
            langugetext.setText("पंजाबी");
        } else if(language.equalsIgnoreCase("ur")){
            langugetext.setText("उर्दू");
        } else if(language.equalsIgnoreCase("bn")){
            langugetext.setText("बंगाली");
        }

        userProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageDialog(0,95);
//                Dexter.withActivity(requireActivity())
//                        .withPermission(Manifest.permission.CAMERA)
//                        .withListener(new PermissionListener() {
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse response) {
//                                // permission is granted, open the camera
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                                    // startActivityForResult(intent, take);
//                                    showImagePickerOptions();
//                                }
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse response) {
//                                // check for permanent denial of permission
//                                //    value=false;
//
//                                Toast.makeText(requireContext(), "We Need Camera", Toast.LENGTH_SHORT).show();
//                                if (response.isPermanentlyDenied()) {
//                                    Intent intent = new Intent();
//                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
//                                    intent.setData(uri);
//                                    startActivity(intent);
//                                    // navigate user to app settings
//                                }
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                                Toast.makeText(requireContext(), "We Need Camera to Scan QR Code", Toast.LENGTH_SHORT).show();
//                                token.continuePermissionRequest();
//                            }
//                        }).check();
            }
        });


        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userType.equalsIgnoreCase("Retailer")) {
                    // update retailer user profile
                    Intent intent = new Intent(getActivity(), UpdateRetailerProfile.class);
                    startActivity(intent);
                } else {
                    // update user profile
                    Intent intent = new Intent(getActivity(), EditProfile.class);
                    intent.putExtra("name", txt_name.getText().toString());
                    intent.putExtra("mobile", txt_number.getText().toString());
                    intent.putExtra("email", txt_emial.getText().toString());
                    startActivity(intent);
                }
            }
        });

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Address.class));
            }
        });

        languageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), LanguageActivity.class);
                intent.putExtra("from","profile");
                startActivity(intent);
               // startActivity(new Intent(getActivity(), LanguageActivity.class));
            }
        });

        feedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Feedback.class));
            }
        });

        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Contactus.class));
            }
        });

        sendqueryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SendQuery.class));
            }
        });

        aboutusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });

        termAndConditionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Termcondition.class));
            }
        });

        privacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PrivacyPolicies.class));
            }
        });

        return view;
    }

    private void openImageDialog(final int upload, final int take) {

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getLayoutInflater();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View convertView = inflater.inflate(R.layout.camera, null);
        LinearLayout camera = convertView.findViewById(R.id.camera);
        LinearLayout gallery = convertView.findViewById(R.id.gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(requireActivity())
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                // permission is granted, open the camera
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivityForResult(intent, take);
                                }
                                alertDialog.dismiss();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // check for permanent denial of permission
                                //    value=false;

                                Toast.makeText(requireContext(), "We Need Camera", Toast.LENGTH_SHORT).show();
                                if (response.isPermanentlyDenied()) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                    // navigate user to app settings
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                Toast.makeText(requireContext(), "We Need Camera to Scan QR Code", Toast.LENGTH_SHORT).show();
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, upload);
                alertDialog.dismiss();

            }
        });

        alertDialog.setView(convertView);
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Helper.INSTANCE.isNetworkAvailable(getActivity())){
            getUserProfile();
        } else {
            Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
        }

    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(requireActivity(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(requireActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(requireActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void getUserProfile() {
        String url = baseurl + "user_profile";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        mainProgressBar.setVisibility(View.VISIBLE);

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mainProgressBar.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String name = jsonObject2.getString("name");
                            String mobile = jsonObject2.getString("mobile");
                            String email = jsonObject2.getString("email");
                            String profilephoto = jsonObject2.getString("profile_photo");
                            String type = jsonObject2.getString("type");
                            userType = type;

                            if (type.equalsIgnoreCase("Retailer")) {
                                String business_name = jsonObject2.getString("business_name");
                                txt_name.setText(business_name);
                            } else {
                                txt_name.setText(name);
                            }

                            txt_number.setText(mobile);
                            txt_emial.setText(email);

                            Glide.with(getActivity()).load(imagebaseurl +profilephoto)
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.app_logo)
                                            .centerCrop()
                                            .error(R.drawable.app_logo))
                                    .into(img_profilephoto);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mainProgressBar.setVisibility(View.GONE);
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Uri targetUri = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                    float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
                    int width = 280;
                    int height = Math.round(width / aspectRatio);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                    convertedimage = ConvertBitmapToString(resizedBitmap);


                    if (Helper.INSTANCE.isNetworkAvailable(getActivity())){
                        updateprofilephoto();
                    } else {
                        Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
                    }

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if (data != null && requestCode == 95) {
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    float aspectRatio = bitmap.getWidth() /
                            (float) bitmap.getHeight();
                    int width = 280;
                    int height = Math.round(width / aspectRatio);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                    convertedimage = ConvertBitmapToString(resizedBitmap);
                    if (Helper.INSTANCE.isNetworkAvailable(getActivity())){
                        updateprofilephoto();
                    } else {
                        Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }


//        if (requestCode == REQUEST_IMAGE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Uri targetUri = data.getParcelableExtra("path");
//                Bitmap bitmap;
//                try {
//                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
//                    float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
//                    int width = 280;
//                    int height = Math.round(width / aspectRatio);
//                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
//                    convertedimage = ConvertBitmapToString(resizedBitmap);
//
//                    if (Helper.INSTANCE.isNetworkAvailable(getActivity())) {
//                        updateprofilephoto();
//                    } else {
//                        Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
//                    }
//
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }


    }

    private String ConvertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return base64String;
    }

    public void updateprofilephoto() {
        String url = baseurl + "user_photo_update";
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String id = yourPrefrence.getData("id");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("profile_photo",convertedimage);

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    Toast.makeText(getActivity(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (r_code.equalsIgnoreCase("1")) {
                        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
                        yourPrefrence.saveData("profile",obj.getString("profile_photo"));
                        Glide.with(getActivity()).load(imagebaseurl +obj.getString("profile_photo"))
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.loading)
                                        .centerCrop()
                                        .error(R.drawable.loading))
                                .into(img_profilephoto);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(getActivity(), DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }

}