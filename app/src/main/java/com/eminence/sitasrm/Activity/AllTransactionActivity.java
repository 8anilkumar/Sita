package com.eminence.sitasrm.Activity;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.eminence.sitasrm.Adapters.TransactionAdapter;
import com.eminence.sitasrm.Models.TransactionsModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllTransactionActivity extends AppCompatActivity {

    ShimmerRecyclerView today_recycle, yesterday_recycle, earlier_recycle;
    LinearLayout todaylayout, yesterdaylayout, earlier_layout, cartEmptyLayout;
    ArrayList<TransactionsModel> transactionlist = new ArrayList<>();
    ArrayList<TransactionsModel> yesterdaytransactionlist = new ArrayList<>();
    ArrayList<TransactionsModel> earliertransaction = new ArrayList<>();
    ProgressBar mainProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transaction);

        today_recycle = findViewById(R.id.today_recycle);
        yesterday_recycle = findViewById(R.id.yesterday_recycle);
        earlier_recycle = findViewById(R.id.earlier_recycle);
        cartEmptyLayout = findViewById(R.id.cartEmptyLayout);
        todaylayout = findViewById(R.id.todaylayout);
        earlier_layout = findViewById(R.id.earlier_layout);
        yesterdaylayout = findViewById(R.id.yesterdaylayout);
        mainProgressBar = findViewById(R.id.mainProgressBar);


        if (Helper.INSTANCE.isNetworkAvailable(AllTransactionActivity.this)) {
            gettransaction();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getyesterdaytransactionlist();
                }
            }, 500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getearlieartransactionList();
                }
            }, 500);

        } else {
            Helper.INSTANCE.Error(AllTransactionActivity.this, getString(R.string.NOCONN));
        }


    }

    public void gettransaction() {

        transactionlist.clear();
        String url = baseurl + "user_transaction_list";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AllTransactionActivity.this);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(AllTransactionActivity.this);
        YourPreference yourPrefrence = YourPreference.getInstance(AllTransactionActivity.this);
        String id = yourPrefrence.getData("id");

        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("type", "today");

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        transactionlist.clear();

                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            TransactionsModel transactionsModel = new TransactionsModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String id = jsonObject2.getString("id");
                            String amount = jsonObject2.getString("amount");
                            String type = jsonObject2.getString("type");
                            String description = jsonObject2.getString("description");
                            String created_date = jsonObject2.getString("created_date");
                            String created_time = jsonObject2.getString("created_time");

                            transactionsModel.setId(id);
                            transactionsModel.setAmount(amount);
                            transactionsModel.setType(type);
                            transactionsModel.setCreated_date(created_date);
                            transactionsModel.setCreated_time(created_time);
                            transactionsModel.setDescription(description);

                            transactionlist.add(transactionsModel);
                        }

                        TransactionAdapter transactionAdapter = new TransactionAdapter(transactionlist, AllTransactionActivity.this);
                        today_recycle.setLayoutManager(new LinearLayoutManager(AllTransactionActivity.this, LinearLayoutManager.VERTICAL, false));
                        today_recycle.setAdapter(transactionAdapter);
                        transactionAdapter.notifyDataSetChanged();

                    } else {
                        today_recycle.setVisibility(View.GONE);
                        todaylayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(AllTransactionActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    public void getyesterdaytransactionlist() {
        yesterdaytransactionlist.clear();
        String url = baseurl + "user_transaction_list";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AllTransactionActivity.this);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(AllTransactionActivity.this);
        YourPreference yourPrefrence = YourPreference.getInstance(AllTransactionActivity.this);
        String id = yourPrefrence.getData("id");

        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("type", "yesterday");

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        yesterdaytransactionlist.clear();

                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            TransactionsModel transactionsModel = new TransactionsModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String id = jsonObject2.getString("id");
                            String amount = jsonObject2.getString("amount");
                            String type = jsonObject2.getString("type");
                            String description = jsonObject2.getString("description");
                            String created_date = jsonObject2.getString("created_date");
                            String created_time = jsonObject2.getString("created_time");

                            transactionsModel.setId(id);
                            transactionsModel.setAmount(amount);
                            transactionsModel.setType(type);
                            transactionsModel.setCreated_date(created_date);
                            transactionsModel.setCreated_time(created_time);
                            transactionsModel.setDescription(description);


                            yesterdaytransactionlist.add(transactionsModel);
                        }

                        TransactionAdapter transactionAdapter = new TransactionAdapter(yesterdaytransactionlist, AllTransactionActivity.this);
                        yesterday_recycle.setLayoutManager(new LinearLayoutManager(AllTransactionActivity.this, LinearLayoutManager.VERTICAL, false));
                        yesterday_recycle.setAdapter(transactionAdapter);
                        transactionAdapter.notifyDataSetChanged();


                    } else {
                        yesterday_recycle.setVisibility(View.GONE);
                        yesterdaylayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(AllTransactionActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        requestQueue.add(stringRequest);

        stringRequest.setShouldCache(false);

    }

    public void getearlieartransactionList() {
        earliertransaction.clear();
        String url = baseurl + "user_transaction_list";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AllTransactionActivity.this);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(AllTransactionActivity.this);
        YourPreference yourPrefrence = YourPreference.getInstance(AllTransactionActivity.this);
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("type", "earlier");
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

                        earliertransaction.clear();

                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            TransactionsModel transactionsModel = new TransactionsModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String id = jsonObject2.getString("id");
                            String amount = jsonObject2.getString("amount");
                            String type = jsonObject2.getString("type");
                            String description = jsonObject2.getString("description");
                            String created_date = jsonObject2.getString("created_date");
                            String created_time = jsonObject2.getString("created_time");

                            transactionsModel.setId(id);
                            transactionsModel.setAmount(amount);
                            transactionsModel.setType(type);
                            transactionsModel.setCreated_date(created_date);
                            transactionsModel.setCreated_time(created_time);
                            transactionsModel.setDescription(description);

                            earliertransaction.add(transactionsModel);
                        }

                        TransactionAdapter transactionAdapter = new TransactionAdapter(earliertransaction, AllTransactionActivity.this);
                        earlier_recycle.setLayoutManager(new LinearLayoutManager(AllTransactionActivity.this, LinearLayoutManager.VERTICAL, false));
                        earlier_recycle.setAdapter(transactionAdapter);
                        transactionAdapter.notifyDataSetChanged();

                    } else {
                        earlier_layout.setVisibility(View.GONE);
                        earlier_recycle.setVisibility(View.GONE);
                    }

                    if ((todaylayout.getVisibility() == View.GONE) && (yesterdaylayout.getVisibility() == View.GONE) && (earlier_layout.getVisibility() == View.GONE)) {
                        cartEmptyLayout.setVisibility(View.VISIBLE);
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

    public void back(View view) {
        onBackPressed();
    }

}