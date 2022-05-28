package com.eminence.sitasrm.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.eminence.sitasrm.Adapters.OrderAdapter;
import com.eminence.sitasrm.Models.CartResponse;
import com.eminence.sitasrm.Models.OrderModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class MyOrdersFragment extends Fragment {

    ShimmerRecyclerView order_recycle;
    ArrayList<OrderModel> orderlist = new ArrayList<>();
    LinearLayout cartEmptyLayout;
    EditText ed_searchOrder;
    OrderAdapter orderAdapter;
    ArrayList<OrderModel> serviceSubCategoryList = new ArrayList<>();
    static DatabaseHandler databaseHandler;
    List<CartResponse> alldata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        order_recycle = view.findViewById(R.id.order_recycle);
        cartEmptyLayout = view.findViewById(R.id.cartEmptyLayout);
        ed_searchOrder = view.findViewById(R.id.ed_searchOrder);

        if (Helper.INSTANCE.isNetworkAvailable(getActivity())) {
            getOrderlist();
            setUpDB();
            //     addtocart();
        } else {
            Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
        }

        ed_searchOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (orderAdapter != null)
                    orderAdapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    order_recycle.setVisibility(View.VISIBLE);
                    //imagesearche.setVisibility(View.GONE);
                } else {
                    getOrderlist();
                    //  order_recycle.setVisibility(View.GONE);
                    //  imagesearche.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }


    public void getOrderlist() {
        orderlist.clear();
        String url = baseurl + "order_list";
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
                        orderlist.clear();
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            OrderModel orderModel = new OrderModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String order_id = jsonObject2.getString("order_id");
                            String net_amount = jsonObject2.getString("net_amount");
                            String order_status = jsonObject2.getString("order_status");
                            String created_at = jsonObject2.getString("created_at");

                            orderModel.setOrder_id(order_id);
                            orderModel.setNet_amount(net_amount);
                            orderModel.setOrder_status(order_status);
                            orderModel.setCreated_at(created_at);
                            orderlist.add(orderModel);

                            setList(orderlist);

                        }
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
                        order_recycle.setVisibility(View.GONE);
                        cartEmptyLayout.setVisibility(View.VISIBLE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    private void setList(ArrayList<OrderModel> orderlist) {
        if (orderlist != null && orderlist.size() > 0) {
            if (orderAdapter == null) {
                serviceSubCategoryList.clear();
                serviceSubCategoryList.addAll(orderlist);
                orderAdapter = new OrderAdapter(orderlist, getActivity());
                order_recycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                order_recycle.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();

            } else {
                serviceSubCategoryList.clear();
                serviceSubCategoryList.addAll(orderlist);
                orderAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(getActivity(), DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }


}