package com.eminence.sitasrm.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.eminence.sitasrm.Activity.AllTransactionActivity;
import com.eminence.sitasrm.Adapters.TransactionAdapter;
import com.eminence.sitasrm.Models.CartResponse;
import com.eminence.sitasrm.Models.TransactionsModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.gson.Gson;
import com.razorpay.Checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;


public class TransactionsFragment extends Fragment {

    ShimmerRecyclerView today_recycle, yesterday_recycle, earlier_recycle;
    LinearLayout todaylayout, yesterdaylayout, earlier_layout, allTransactionsLayout,cartEmptyLayout;
    ArrayList<TransactionsModel> transactionlist = new ArrayList<>();
    ArrayList<TransactionsModel> yesterdaytransactionlist = new ArrayList<>();
    ArrayList<TransactionsModel> earliertransaction = new ArrayList<>();
    EditText amount, amounttext;
    TextView addamt, textlong, rupees, txt_amount;
    DecimalFormat df;
    ProgressBar mainProgressBar;
    static DatabaseHandler databaseHandler;
    List<CartResponse> alldata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        today_recycle = view.findViewById(R.id.today_recycle);
        yesterday_recycle = view.findViewById(R.id.yesterday_recycle);
        earlier_recycle = view.findViewById(R.id.earlier_recycle);
        todaylayout = view.findViewById(R.id.todaylayout);
        earlier_layout = view.findViewById(R.id.earlier_layout);
        yesterdaylayout = view.findViewById(R.id.yesterdaylayout);
        allTransactionsLayout = view.findViewById(R.id.allTransactionsLayout);
        cartEmptyLayout = view.findViewById(R.id.cartEmptyLayout);
        mainProgressBar = view.findViewById(R.id.mainProgressBar);
        amount = view.findViewById(R.id.amount);
        amounttext = view.findViewById(R.id.amounttext);
        addamt = view.findViewById(R.id.addamt);
        textlong = view.findViewById(R.id.textlong);
        rupees = view.findViewById(R.id.rupees);
        txt_amount = view.findViewById(R.id.txt_amount);
        df = new DecimalFormat("#.##");

        setUpDB();

        addamt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount.getText().toString().equalsIgnoreCase("")) {
                    amount.setError("Amount Cannot be 0");
                } else {
                    YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
                    yourPrefrence.saveData("wallet_recharge_amount", amount.getText().toString());
                    startPayment(amount.getText().toString());
                }

            }
        });

        allTransactionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllTransactionActivity.class);
                startActivity(intent);

            }
        });

        if (Helper.INSTANCE.isNetworkAvailable(getActivity())) {
            gettransaction();
            getamount();
        } else {
            Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
        }

        return view;
    }

    public void getamount() {
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String user_id = yourPrefrence.getData("id");
        String url = baseurl + "wallet_amount";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        String amount = obj.getString("balance");
                        if (amount.equalsIgnoreCase("0")) {
                            amounttext.setTextColor(getResources().getColor(R.color.red));
                            rupees.setTextColor(getResources().getColor(R.color.red));
                        } else if (Integer.parseInt(amount) < 500) {
                            amounttext.setText("₹ " + df.format(Double.parseDouble(amount)));
                        } else {
                            amounttext.setText("₹ " + df.format(Double.parseDouble(amount)));
                        }
                        txt_amount.setText(amount+" Points");
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

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

    }

    public void gettransaction() {
        transactionlist.clear();
        String url = baseurl + "user_transaction_list";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String id = yourPrefrence.getData("id");

        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("type", "today");

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getyesterdaytransactionlist();

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
                            String description_hindi = jsonObject2.getString("description_hindi");
                            String created_date = jsonObject2.getString("created_date");
                            String created_time = jsonObject2.getString("created_time");

                            transactionsModel.setId(id);
                            transactionsModel.setAmount(amount);
                            transactionsModel.setType(type);
                            transactionsModel.setCreated_date(created_date);
                            transactionsModel.setCreated_time(created_time);
                            transactionsModel.setDescription(description);
                            transactionsModel.setDescription_hindi(description_hindi);

                            if(amount.equalsIgnoreCase("0")){

                            } else {
                                transactionlist.add(transactionsModel);
                            }
                        }


                        TransactionAdapter transactionAdapter = new TransactionAdapter(transactionlist, getActivity());
                        today_recycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        today_recycle.setAdapter(transactionAdapter);
                        transactionAdapter.notifyDataSetChanged();


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
                getyesterdaytransactionlist();
                //Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    public void getyesterdaytransactionlist() {

        yesterdaytransactionlist.clear();
        String url = baseurl + "user_transaction_list";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String id = yourPrefrence.getData("id");

        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("type", "yesterday");

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getearlieartransactionList();

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
                            String description_hindi = jsonObject2.getString("description_hindi");
                            String created_time = jsonObject2.getString("created_time");

                            transactionsModel.setId(id);
                            transactionsModel.setAmount(amount);
                            transactionsModel.setType(type);
                            transactionsModel.setCreated_date(created_date);
                            transactionsModel.setCreated_time(created_time);
                            transactionsModel.setDescription(description);
                            transactionsModel.setDescription_hindi(description_hindi);

                            yesterdaytransactionlist.add(transactionsModel);
                        }

                        TransactionAdapter transactionAdapter = new TransactionAdapter(yesterdaytransactionlist, getActivity());
                        yesterday_recycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        yesterday_recycle.setAdapter(transactionAdapter);
                        transactionAdapter.notifyDataSetChanged();

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
                getearlieartransactionList();

            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    public void getearlieartransactionList() {
        earliertransaction.clear();
        String url = baseurl + "user_transaction_list";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
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
                            String description_hindi = jsonObject2.getString("description_hindi");

                            transactionsModel.setId(id);
                            transactionsModel.setAmount(amount);
                            transactionsModel.setType(type);
                            transactionsModel.setCreated_date(created_date);
                            transactionsModel.setCreated_time(created_time);
                            transactionsModel.setDescription(description);
                            transactionsModel.setDescription_hindi(description_hindi);

                            earliertransaction.add(transactionsModel);
                        }

                        TransactionAdapter transactionAdapter = new TransactionAdapter(earliertransaction, getActivity());
                        earlier_recycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        earlier_recycle.setAdapter(transactionAdapter);
                        transactionAdapter.notifyDataSetChanged();

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

    public void startPayment(String amount) {
        final Checkout co = new Checkout();
        try {
            YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
            String email = yourPrefrence.getData("email");
            String mobile = yourPrefrence.getData("mobile");
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Wallet recharge");
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            String payment = amount;
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mobile);
            options.put("prefill", preFill);
            co.open(getActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(getActivity(), DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }
 }