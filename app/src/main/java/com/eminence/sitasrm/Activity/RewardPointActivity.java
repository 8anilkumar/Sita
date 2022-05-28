package com.eminence.sitasrm.Activity;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.eminence.sitasrm.Adapters.EarnAdapter;
import com.eminence.sitasrm.Models.Earnedmodel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RewardPointActivity extends AppCompatActivity {
    ShimmerRecyclerView recycle;
    ArrayList<Earnedmodel> arrayList = new ArrayList<>();
    TextView totalearned, totalredeemed, balance;
    ImageView imgBack, imgScanner;
    LinearLayout helpLayout, redeemLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_point);

        imgBack = findViewById(R.id.imgBack);
        imgScanner = findViewById(R.id.imgScanner);
        helpLayout = findViewById(R.id.helpLayout);
        redeemLayout = findViewById(R.id.redeemLayout);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help.class));
            }
        });

        imgScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Getcamerapermission();
            }
        });

        redeemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RedeemPoints.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        balance = findViewById(R.id.balance);
        totalearned = findViewById(R.id.totalearned);
        totalredeemed = findViewById(R.id.totalredeemed);
        recycle = findViewById(R.id.recycle);

    }

    private void Getcamerapermission() {
        Dexter.withActivity(RewardPointActivity.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
                        startActivity(new Intent(getApplicationContext(), Scanner.class));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        //    value=false;

                        Toast.makeText(getApplicationContext(), "We Need Camera to Scan QR Code", Toast.LENGTH_SHORT).show();
                        if (response.isPermanentlyDenied()) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        Toast.makeText(getApplicationContext(), "We Need Camera to Scan QR Code", Toast.LENGTH_SHORT).show();
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    public void reward() {
        String url = baseurl + "user_coupons";
        arrayList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(RewardPointActivity.this);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
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
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Earnedmodel winnerModel = new Earnedmodel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String id = jsonObject2.getString("id");
                            String amount = jsonObject2.getString("amount");
                            String order_id = jsonObject2.getString("order_id");
                            String redeem_status = jsonObject2.getString("redeem_status");
                            String created_date = jsonObject2.getString("created_date");
                            String created_time = jsonObject2.getString("created_time");
                            winnerModel.setAmount(amount);
                            winnerModel.setId(id);
                            winnerModel.setOrder_di(order_id);
                            winnerModel.setRedeem_status(redeem_status);
                            winnerModel.setCreated_date(created_date);
                            winnerModel.setCreated_time(created_time);
                            arrayList.add(winnerModel);

                        }

                       // Collections.reverse(arrayList);
                        EarnAdapter newsAdapter = new EarnAdapter(arrayList, RewardPointActivity.this, "reward");
                        recycle.setLayoutManager(new GridLayoutManager(RewardPointActivity.this, 2));
                        recycle.setAdapter(newsAdapter);
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        recycle.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RewardPointActivity.this, "" + e, Toast.LENGTH_SHORT).show();
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

    private void getamout() {

        String url = baseurl + "wallet_amount";
        RequestQueue requestQueue = Volley.newRequestQueue(RewardPointActivity.this);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
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

            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        reward();
        getamout();

    }
}