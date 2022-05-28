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
import android.widget.RelativeLayout;
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
import java.util.HashMap;
import java.util.Map;

public class RedeemPoints extends AppCompatActivity {

    ShimmerRecyclerView recycle;
    ArrayList<Earnedmodel> arrayList = new ArrayList<>();
    TextView totalearned,totalredeemed,balance;
    LinearLayout helpLayout,redeemLayout;
    RelativeLayout earnedLayout;
    ImageView imgScanner,imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_points);

        balance=findViewById(R.id.balance);
        totalearned=findViewById(R.id.totalearned);
        totalredeemed=findViewById(R.id.totalredeemed);
        recycle=findViewById(R.id.recycle);

        imgBack = findViewById(R.id.imgBack);
        imgScanner = findViewById(R.id.imgScanner);
        helpLayout = findViewById(R.id.helpLayout);
        earnedLayout = findViewById(R.id.earnedLayout);


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
                startActivity(new Intent(getApplicationContext(), Scanner.class));
            }
        });
        earnedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), RewardPointActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });


      //  Winnerlist();
        getamout();
    }

    public void help(View view) {
        startActivity(new Intent(getApplicationContext(), Help.class));
    }


    private void getamout() {

        String url = baseurl + "wallet_amount";
        RequestQueue requestQueue = Volley.newRequestQueue(RedeemPoints.this);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id",id );

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

}