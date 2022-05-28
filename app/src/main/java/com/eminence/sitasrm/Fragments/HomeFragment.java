package com.eminence.sitasrm.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eminence.sitasrm.Activity.AllProduct;
import com.eminence.sitasrm.Activity.DashboardActivity;
import com.eminence.sitasrm.Activity.Help;
import com.eminence.sitasrm.Activity.Notification;
import com.eminence.sitasrm.Activity.Profile.Feedback;
import com.eminence.sitasrm.Activity.Profile.Address;
import com.eminence.sitasrm.Activity.ReferFriendActivity;
import com.eminence.sitasrm.Activity.RewardPointActivity;
import com.eminence.sitasrm.Activity.Scanner;
import com.eminence.sitasrm.Activity.SubscriptionPlanActivity;
import com.eminence.sitasrm.Activity.SuccesfullSubscribe;
import com.eminence.sitasrm.Adapters.EarnAdapter;
import com.eminence.sitasrm.Adapters.FeedbackSliderAdapter;
import com.eminence.sitasrm.Adapters.ImageSlideAdapter;
import com.eminence.sitasrm.Adapters.ProductAdapter;
import com.eminence.sitasrm.Adapters.VideoAdapter;
import com.eminence.sitasrm.Adapters.WinnerAdapter;
import com.eminence.sitasrm.Interface.AddLifecycleCallback;
import com.eminence.sitasrm.Interface.BadgingInterface;
import com.eminence.sitasrm.Models.AddressModel;
import com.eminence.sitasrm.Models.CartModel;
import com.eminence.sitasrm.Models.CartResponse;
import com.eminence.sitasrm.Models.Earnedmodel;
import com.eminence.sitasrm.Models.Feedbackmodel;
import com.eminence.sitasrm.Models.Images;
import com.eminence.sitasrm.Models.ProductModel;
import com.eminence.sitasrm.Models.VideoModel;
import com.eminence.sitasrm.Models.WinnerModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.GPSTracker;
import com.eminence.sitasrm.Utils.GpsUtils;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.github.demono.AutoScrollViewPager;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.eminence.sitasrm.MainActivity.badgecountmain;
import static com.eminence.sitasrm.Utils.Baseurl.baseurl;
import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

public class HomeFragment extends Fragment implements BadgingInterface, AddLifecycleCallback {

    private AutoScrollViewPager viewPager, recipiviewpager;
    TextView location_pincode, txt_seeAll, txt_memberShipPlan, txt_dis_coupon,
            txt_discriptionDiscount, totalearned, totalredeemed, balance, rewardBadge;
    RecyclerView product_recycle,winnerRecycle;
    FrameLayout notificationLayout;
    ImageView img_help, scanImg, imgScanner;
    YourPreference yourPrefrence;
    TextView become_subs_btn, txtFeedBack;
    TextView lead_badge;
    CardView become_subscriber_layout, prime_member_layout;
    ArrayList<ProductModel> offerlist = new ArrayList<>();
    ArrayList<Feedbackmodel> Feedbacklist = new ArrayList<>();
    LinearLayout feedBackLayout, supportLayout, addressLayout, myDashboardLayout,
            earnedLayout, referFriendLayout, bonusPointLayout, videoLayout,winnerLayout;
    DatabaseHandler databaseHandler;
    RecyclerView videoRecyclerView;
    ArrayList<VideoModel> videolist = new ArrayList<>();
    ArrayList<WinnerModel> arrayList = new ArrayList<>();
    String product_catalogue;
    LinearLayout catalogue_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        location_pincode = view.findViewById(R.id.location_pincode);
        product_recycle = view.findViewById(R.id.product_recycle);
        txt_seeAll = view.findViewById(R.id.txt_seeAll);
        notificationLayout = view.findViewById(R.id.notificationLayout);
        catalogue_layout = view.findViewById(R.id.catalogue_layout);
        txt_memberShipPlan = view.findViewById(R.id.txt_memberShipPlan);
        become_subscriber_layout = view.findViewById(R.id.become_subscriber_layout);
        prime_member_layout = view.findViewById(R.id.prime_member_layout);
        become_subs_btn = view.findViewById(R.id.become_subs_btn);
        img_help = view.findViewById(R.id.img_help);
        feedBackLayout = view.findViewById(R.id.feedBackLayout);
        supportLayout = view.findViewById(R.id.supportLayout);
        recipiviewpager = view.findViewById(R.id.recipiviewpager);
        txtFeedBack = view.findViewById(R.id.txtFeedBack);
        txt_dis_coupon = view.findViewById(R.id.txt_dis_coupon);
        addressLayout = view.findViewById(R.id.addressLayout);
        lead_badge = view.findViewById(R.id.lead_badge);
        txt_discriptionDiscount = view.findViewById(R.id.txt_discriptionDiscount);
        myDashboardLayout = view.findViewById(R.id.myDashboardLayout);
        earnedLayout = view.findViewById(R.id.earnedLayout);
        referFriendLayout = view.findViewById(R.id.referFriendLayout);
        bonusPointLayout = view.findViewById(R.id.bonusPointLayout);
        totalearned = view.findViewById(R.id.totalearned);
        totalredeemed = view.findViewById(R.id.totalredeemed);
        balance = view.findViewById(R.id.balance);
        scanImg = view.findViewById(R.id.scanImg);
        imgScanner = view.findViewById(R.id.imgScanner);
        videoRecyclerView = view.findViewById(R.id.videoRecyclerView);
        videoLayout = view.findViewById(R.id.videoLayout);
        rewardBadge = view.findViewById(R.id.rewardBadge);
        winnerLayout = view.findViewById(R.id.winnerLayout);
        winnerRecycle = view.findViewById(R.id.winnerRecycle);
        viewPager.setCycle(true);
        setUpDB();

        if (Helper.INSTANCE.isNetworkAvailable(getActivity())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GetBanner();
                    appfeedback();
                    getWinner();
                    checkPermission();
                }
            }, 1000);
        } else {
            Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
        }

        yourPrefrence = YourPreference.getInstance(getActivity());
        String badgevalue = yourPrefrence.getData("badge");
        if (badgevalue.equalsIgnoreCase("")) {
            badgevalue = "0";
        }

        if (badgevalue.equalsIgnoreCase("0")) {
            lead_badge.setVisibility(View.GONE);
        }

        lead_badge.setText(badgevalue);

        scanImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Getcamerapermission();
            }
        });


        become_subs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                become_subs_apicall();
            }
        });

        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Help.class);
                startActivity(intent);
            }
        });

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Address.class);
                startActivity(intent);
            }
        });

        location_pincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Address.class);
                startActivity(intent);
            }
        });

        txt_memberShipPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SubscriptionPlanActivity.class);
                startActivity(intent);
            }
        });

        txt_seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllProduct.class);
                startActivity(intent);
            }
        });

        notificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Notification.class);
                startActivity(intent);
            }
        });

        feedBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Feedback.class));
            }
        });
        catalogue_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imagebaseurl + product_catalogue)));

            }
        });

        supportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Help.class));
            }
        });

        myDashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DashboardActivity.class));
            }
        });

        bonusPointLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DashboardActivity.class));
            }
        });

        earnedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RewardPointActivity.class));
            }
        });

        referFriendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ReferFriendActivity.class));
            }
        });

        imgScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Getcamerapermission();
            }
        });

        return view;

    }


    private void checkPermission() {

        Dexter.withActivity(requireActivity())
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
                        turngpspon();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        //checkPermission();
                        if (response.isPermanentlyDenied()) {

//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package",requireActivity().getPackageName(), null);
//                            intent.setData(uri);
//                            startActivity(intent);
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        //checkPermission();
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void updateLocation(String state, String city, String pin, double latitude, double longitude, String fullAddress) {
        String url = baseurl + "latlong_update";
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(requireContext());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("latitude", String.valueOf(latitude));
        params.put("longitude", String.valueOf(longitude));

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             error.printStackTrace();
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }

    private void turngpspon() {
        final LocationManager manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(requireActivity())) {
            //  Toast.makeText(getApplicationContext(), "Gps already enabled", Toast.LENGTH_SHORT).show();
            redirectionScreen();
        } else {
            if (!hasGPSDevice(requireActivity())) {
                //Toast.makeText(getApplicationContext(), "Gps not Supported", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(requireActivity())) {
                //     Toast.makeText(getApplicationContext(), "Gps not enabled", Toast.LENGTH_SHORT).show();
                new GpsUtils(requireActivity()).turnGPSOn(new GpsUtils.OnGpsListener() {
                    @Override
                    public void gpsStatus(boolean isGPSEnable) {
                        if (isGPSEnable) {
                            redirectionScreen();
                        }
                    }
                });
            }
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void redirectionScreen() {
        GPSTracker mGPS = new GPSTracker(requireContext());
        Double lat, lng;
        if (mGPS.canGetLocation) {
            mGPS.getLocation();
            lat = mGPS.getLatitude();
            lng = mGPS.getLongitude();
            getCompleteAddressString(lat, lng);

        } else {
            Toast.makeText(mGPS, "location not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            String State = addresses.get(0).getAdminArea();
            String city = addresses.get(0).getLocality();
            String pin = addresses.get(0).getPostalCode();
            String fullAddress = addresses.get(0).getAddressLine(0);

            updateLocation(State, city, pin,LATITUDE,LONGITUDE,fullAddress);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireActivity(), "Please on GPS location", Toast.LENGTH_SHORT).show();
        }
    }


    private void getWinner() {
        arrayList.clear();
        String url = baseurl + "winner_list";
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            WinnerModel winnerModel = new WinnerModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String id = jsonObject2.getString("id");
                            String name = jsonObject2.getString("name");
                            String profile_pic = jsonObject2.getString("profile_pic");
                            String product_name = jsonObject2.getString("product_name");
                            String product_image = jsonObject2.getString("product_image");
                            String created_at = jsonObject2.getString("created_at");

                            winnerModel.setId(id);
                            winnerModel.setName(name);
                            winnerModel.setProfile_pic(profile_pic);
                            winnerModel.setProduct_name(product_name);
                            winnerModel.setProduct_image(product_image);
                            winnerModel.setCreated_at(created_at);

                            arrayList.add(winnerModel);

                        }

                        if(arrayList.size() > 0){
                            winnerLayout.setVisibility(View.VISIBLE);
                            WinnerAdapter newsAdapter = new WinnerAdapter(arrayList, requireActivity());
                            winnerRecycle.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
                            winnerRecycle.setAdapter(newsAdapter);
                            newsAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);


    }

    private void Getcamerapermission() {
        Dexter.withActivity(requireActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
                        startActivity(new Intent(requireContext(), Scanner.class));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        // value=false;

                        Toast.makeText(requireContext(), "We Need Camera to Scan QR Code", Toast.LENGTH_SHORT).show();
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

    public void video() {
        videolist.clear();
        String url = baseurl + "videos";
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));

                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            VideoModel recipiemodel = new VideoModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String id = jsonObject2.getString("id");
                            String video = jsonObject2.getString("video_url");
                            recipiemodel.setId(id);
                            recipiemodel.setVideo_url(video);
                            videolist.add(recipiemodel);
                        }
                        if (videolist.size() != 0) {
                            binddata(videolist);
                        }
                    } else {
                        videoRecyclerView.setVisibility(View.GONE);
                        videoLayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }

    public void reward() {
        String url = baseurl + "user_coupons";
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(requireContext());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);

        JSONObject parameters = new JSONObject(params);
        Log.i("transaction_param", "" + parameters);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("transaction_response", "" + response);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        if (obj.getString("unredeemed_coupon").equalsIgnoreCase("0")) {
                            rewardBadge.setVisibility(View.GONE);
                        } else {
                            rewardBadge.setVisibility(View.VISIBLE);
                            rewardBadge.setText(obj.getString("unredeemed_coupon"));
                        }
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

    private void binddata(ArrayList<VideoModel> videolist) {
        VideoAdapter videoAdapter = new VideoAdapter(videolist, requireActivity(), this);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        videoRecyclerView.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();
        videoRecyclerView.setVisibility(View.VISIBLE);
        videoLayout.setVisibility(View.VISIBLE);

    }

    public void appfeedback() {
        Feedbacklist.clear();
        String url = baseurl + "app_feedback_list";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            Feedbackmodel feedbackmodel = new Feedbackmodel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String name = jsonObject2.getString("name");
                            String profile_photo = jsonObject2.getString("profile_photo");
                            String star_rate = jsonObject2.getString("star_rate");
                            String feedback = jsonObject2.getString("feedback");

                            feedbackmodel.setName(name);
                            feedbackmodel.setProfilephoto(profile_photo);
                            feedbackmodel.setStarrate(star_rate);
                            feedbackmodel.setFeedback(feedback);
                            Feedbacklist.add(feedbackmodel);

                        }

                        final FeedbackSliderAdapter feedbackAdapter = new FeedbackSliderAdapter(Feedbacklist, getActivity());
                        recipiviewpager.setAdapter(feedbackAdapter);
                        recipiviewpager.startAutoScroll();
                        recipiviewpager.setSlideDuration(3 * 1000);
                        recipiviewpager.setSlideInterval(4 * 1000);
                        feedbackAdapter.notifyDataSetChanged();
                        recipiviewpager.setVisibility(View.VISIBLE);
                        recipiviewpager.setVisibility(View.VISIBLE);

                    } else {
                        recipiviewpager.setVisibility(View.GONE);
                        txtFeedBack.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    public void GetBanner() {
        String url = baseurl + "banner_list";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        List<Images> images = new GsonBuilder().create().fromJson(obj.getJSONArray("data").toString(), new TypeToken<List<Images>>() {
                        }.getType());
                        viewPager.setVisibility(View.VISIBLE);

                        if (images != null && images.size() > 0) {
                            viewPager.setAdapter((new ImageSlideAdapter(images, getActivity())));
                            viewPager.startAutoScroll();
                            viewPager.setSlideDuration(2 * 1000);
                            viewPager.setSlideInterval(3 * 1000);
                        } else {
                            viewPager.setVisibility(View.GONE);
                        }
                    } else {
                        viewPager.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    public void Productlist() {
        offerlist.clear();
        String url = baseurl + "product_list";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        offerlist.clear();
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProductModel productModel = new ProductModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String product_id = jsonObject2.getString("product_id");
                            String category_id = jsonObject2.getString("category_id");
                            String product_name = jsonObject2.getString("product_name");
                            String caption_eng = jsonObject2.getString("caption_eng");
                            String caption_hindi = jsonObject2.getString("caption_hindi");
                            String price = jsonObject2.getString("price");
                            String pouch_quantity = jsonObject2.getString("pouch_quantity");
                            String product_image = jsonObject2.getString("product_image");
                            String created_at = jsonObject2.getString("created_at");
                            String p_name_hindi = jsonObject2.getString("product_name_hindi");
                            String description_hindi = jsonObject2.getString("description_hindi");
                            String cart_availability = jsonObject2.getString("cart_availability");
                            String quantity = jsonObject2.getString("quantity");
                            String single_description_english = jsonObject2.getString("single_description_english");
                            String single_description_hindi = jsonObject2.getString("single_description_hindi");

                            productModel.setCategory_id(category_id);
                            productModel.setProduct_id(product_id);
                            productModel.setProduct_name(product_name);
                            productModel.setPrice(price);
                            productModel.setProduct_image(product_image);
                            productModel.setPouch_quantity(pouch_quantity);
                            productModel.setCreated_at(created_at);
                            productModel.setP_name_hindi(p_name_hindi);
                            productModel.setDescription_hindi(description_hindi);
                            productModel.setCaption_eng(caption_eng);
                            productModel.setCaption_hindi(caption_hindi);
                            productModel.setQuantity(quantity);
                            productModel.setCart_availability(cart_availability);
                            productModel.setSingle_description_english(single_description_english);
                            productModel.setSingle_description_hindi(single_description_hindi);

                            offerlist.add(productModel);
                        }

                        bindadapter(offerlist);

                    } else if (r_code.equalsIgnoreCase("100")) {
                        Toast.makeText(getActivity(), "Your Account has been Terminated!!", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences preferences = getActivity().getSharedPreferences("YourCustomNamedPreference", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                getActivity().finishAffinity();
                            }
                        }, 1500);

                    } else {
                        //offerlayoput.setVisibility(View.GONE);
                        product_recycle.setVisibility(View.GONE);
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

    private void bindadapter(ArrayList<ProductModel> offerlist) {
        if (offerlist.size() != 0){
            ProductAdapter offerlistAdapter = new ProductAdapter(offerlist, getActivity(), this, "home");
            product_recycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            product_recycle.setAdapter(offerlistAdapter);
            offerlistAdapter.notifyDataSetChanged();
        }
    }

    public void address() {
        String url = baseurl + "user_address_list";
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(requireContext());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AddressModel addressModel = new AddressModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String state = jsonObject2.getString("state");
                            String pincode = jsonObject2.getString("pincode");
                            String defaults = jsonObject2.getString("defaults");

                            if (defaults.equalsIgnoreCase("1")) {
                                addressModel.setState(state);
                                addressModel.setPincode(pincode);
                                location_pincode.setText("Deliver to " + state + " " + pincode);
                            }
                        }

                    } else {
                        location_pincode.setText(R.string.address2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void getamout() {
        String url = baseurl + "wallet_amount";
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(requireContext());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        String total_earned = obj.getString("total_earned");
                        String total_redeemed = obj.getString("total_redeemed");
                        String balancee = obj.getString("balance");
                        totalearned.setText(total_earned);
                        totalredeemed.setText(total_redeemed);
                        balance.setText(balancee);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Helper.INSTANCE.isNetworkAvailable(getActivity())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setUpDB();
                    address();
                    reward();
                    Productlist();
                    getamout();
                    video();
                    getUtility();
                }
            }, 1000);

        } else {
            Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
        }


        yourPrefrence = YourPreference.getInstance(getActivity());
        String badgevalue = yourPrefrence.getData("badge");
        if (badgevalue.equalsIgnoreCase("")) {
            badgevalue = "0";
        }

        if (badgevalue.equalsIgnoreCase("0")) {
            lead_badge.setVisibility(View.GONE);
        } else {
            lead_badge.setVisibility(View.VISIBLE);
        }
        lead_badge.setText(badgevalue);
    }

    private void getUtility() {
        String url = baseurl + "utility";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");
                    if (status_code.equalsIgnoreCase("1")) {
                        YourPreference yourPrefrence = YourPreference.getInstance(requireContext());
                        yourPrefrence.saveData("userProductCountLimit",obj.getString("user_max_purchase"));
                        yourPrefrence.saveData("retailerProductCountLimit",obj.getString("retailer_max_purchase"));
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

    public void become_subs_apicall() {
        String url = baseurl + "user_subscribe";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String id = yourPrefrence.getData("id");

        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        JSONObject parameters = new JSONObject(params);


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");
                    if (status_code.equalsIgnoreCase("1")) {
                        Intent intent = new Intent(getActivity(), SuccesfullSubscribe.class);
                        startActivity(intent);
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

    public void check_subscribe_status() {
        String url = baseurl + "user_subscribe_check";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");
                    if (status_code.equalsIgnoreCase("1")) {
                        become_subscriber_layout.setVisibility(View.GONE);
                        prime_member_layout.setVisibility(View.GONE);
                    } else {
                        become_subscriber_layout.setVisibility(View.GONE);
                        prime_member_layout.setVisibility(View.GONE);
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

    @Override
    public void badgecount() {
        getamount_from_adapter(getActivity());
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(getActivity(), DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }

    public void getamount_from_adapter(Context context) {

        String url = baseurl + "cart_calculation";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String id = yourPrefrence.getData("id");

        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");
                    if (status_code.equalsIgnoreCase("1")) {
                        String total_products = obj.getString("total_products");
                        String totalpricee = obj.getString("total_amount");
                        int total_cart_item = Integer.parseInt(total_products);
                        badgecountmain(total_cart_item);
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


    @Override
    public void onStop() {
        setUpDB();
        //addtocart();
        super.onStop();

    }

    @Override
    public void addLifeCycleCallBack(YouTubePlayerView youTubePlayerView, LinearLayout linearLayout) {
        getLifecycle().addObserver(youTubePlayerView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int a = (width * 15) / 100;
        int b = (height * 76) / 100;

        params.height = height - b;
        params.width = width - a;

        params.setMargins(10, 0, 10, 0);
        linearLayout.setLayoutParams(params);
    }

}