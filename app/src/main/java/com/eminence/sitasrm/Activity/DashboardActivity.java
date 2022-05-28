package com.eminence.sitasrm.Activity;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.eminence.sitasrm.Adapters.DashboardAdapter;
import com.eminence.sitasrm.Models.DashboardModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView totalearned,totalredeemed,balance;
    ShimmerRecyclerView recycle;
    ArrayList<DashboardModel> arrayList = new ArrayList<>();
    LinearLayout helpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        imgBack = findViewById(R.id.imgBack);
        helpLayout = findViewById(R.id.helpLayout);
        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help.class));
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        balance=findViewById(R.id.balance);
        totalearned=findViewById(R.id.totalearned);
        totalredeemed=findViewById(R.id.totalredeemed);
        recycle=findViewById(R.id.recycle);

        if (Helper.INSTANCE.isNetworkAvailable(DashboardActivity.this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getamout();
                    transaction();
                }
            }, 1000);

        } else {
            Helper.INSTANCE.Error(DashboardActivity.this, getString(R.string.NOCONN));
        }

    }


    public void transaction() {
        String url = baseurl + "user_transaction_list";
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id",id);
        params.put("type","all");

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

                            DashboardModel dashboardModel = new DashboardModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String amount = jsonObject2.getString("amount");
                            String type = jsonObject2.getString("type");
                            String created_date = jsonObject2.getString("created_date");
                            String created_time = jsonObject2.getString("created_time");
                            String description = jsonObject2.getString("description");
                            String description_hindi = jsonObject2.getString("description_hindi");

                            dashboardModel.setAmount(amount);
                            dashboardModel.setType(type);
                            dashboardModel.setCreated_date(created_date);
                            dashboardModel.setCreated_time(created_time);
                            dashboardModel.setDescription(description);
                            dashboardModel.setDescription_hindi(description_hindi);
                            arrayList.add(dashboardModel);
                        }

                        DashboardAdapter newsAdapter = new DashboardAdapter(arrayList, DashboardActivity.this,"reward");
                        recycle.setLayoutManager(new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.VERTICAL, false));
                        recycle.setAdapter(newsAdapter);
                        newsAdapter.notifyDataSetChanged();

                    } else {
                        recycle.setVisibility(View.GONE);
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

    private void getamout() {

        String url = baseurl + "wallet_amount";
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
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