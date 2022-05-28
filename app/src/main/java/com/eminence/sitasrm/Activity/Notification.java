package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.eminence.sitasrm.Adapters.NotificationAdapter;
import com.eminence.sitasrm.Models.CartResponse;
import com.eminence.sitasrm.Models.NotificationModel;
import com.eminence.sitasrm.Models.TransactionsModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class Notification extends AppCompatActivity {

    ShimmerRecyclerView notification_recycle;
    ArrayList<TransactionsModel> transactionlist = new ArrayList<>();
    DatabaseHandler databaseHandler;
    List<CartResponse> alldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notification_recycle=findViewById(R.id.notification_recycle);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        yourPrefrence.saveData("badge","0");

        if (Helper.INSTANCE.isNetworkAvailable(Notification.this)){
            getNotification();
            setUpDB();
            addtocart();
        } else {
            Helper.INSTANCE.Error(Notification.this, getString(R.string.NOCONN));
        }
    }

    public void getNotification() {
        transactionlist.clear();
        String url = baseurl + "user_notification";
        RequestQueue requestQueue = Volley.newRequestQueue(Notification.this);
        YourPreference yourPrefrence = YourPreference.getInstance(Notification.this);
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

                    Log.i("notification response",String.valueOf(response));
                    if (r_code.equalsIgnoreCase("1")) {
                        List<NotificationModel> notificationModelList = new GsonBuilder().create().fromJson(obj.getJSONArray("data").toString(), new TypeToken<List<NotificationModel>>() {}.getType());
                        NotificationAdapter transactionAdapter = new NotificationAdapter(notificationModelList, Notification.this);
                        notification_recycle.setLayoutManager(new LinearLayoutManager(Notification.this, LinearLayoutManager.VERTICAL, false));
                        notification_recycle.setAdapter(transactionAdapter);
                        transactionAdapter.notifyDataSetChanged();
                    } else {
                         notification_recycle.setVisibility(View.GONE);
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

    public void back(View view) {
        onBackPressed();
    }

    public void help(View view) {
        startActivity(new Intent(getApplicationContext(), Help.class));
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(Notification.this, DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }

    private void addtocart() {
        alldata = databaseHandler.cartInterface().getallcartdata();
        JSONArray jsonArray = null;
        String json = String.valueOf(new Gson().toJsonTree(alldata));
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        YourPreference yourPrefrence = YourPreference.getInstance(Notification.this);
        String user_id = yourPrefrence.getData("id");
        String url = baseurl + "add_to_cart";
        RequestQueue requestQueue = Volley.newRequestQueue(Notification.this);
        JSONObject params = new JSONObject();

        try {
            params.put("user_id", user_id);
            params.put("products", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("My Cart Parameter",""+params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");
                    if (status_code.equalsIgnoreCase("1")) {

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ABC", String.valueOf(error));
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }
}