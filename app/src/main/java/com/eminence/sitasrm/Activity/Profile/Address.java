package com.eminence.sitasrm.Activity.Profile;


import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.eminence.sitasrm.Activity.Help;
import com.eminence.sitasrm.Adapters.AddressAdapter;
import com.eminence.sitasrm.Interface.DeleteAddressListner;
import com.eminence.sitasrm.Interface.MakeDefaultAddressListner;
import com.eminence.sitasrm.Models.AddressModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Address extends AppCompatActivity implements MakeDefaultAddressListner, DeleteAddressListner {

    ArrayList<AddressModel> offerlist = new ArrayList<>();
    ShimmerRecyclerView addressrecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        addressrecycle=findViewById(R.id.addressrecycle);

    }

    public void address() {
        offerlist.clear();
        String url = baseurl + "user_address_list";
        RequestQueue requestQueue = Volley.newRequestQueue(Address.this);
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
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            AddressModel addressModel = new AddressModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String id = jsonObject2.getString("id");
                            String type = jsonObject2.getString("type");
                            String hf_number = jsonObject2.getString("hf_number");
                            String address = jsonObject2.getString("address");
                            String landmark = jsonObject2.getString("landmark");
                            String state = jsonObject2.getString("state");
                            String pincode = jsonObject2.getString("pincode");
                            String created_at = jsonObject2.getString("created_at");
                            String name = jsonObject2.getString("name");
                            String mobile1 = jsonObject2.getString("mobile1");
                            String mobile2 = jsonObject2.getString("mobile2");
                            String defaults = jsonObject2.getString("defaults");

                            addressModel.setId(id);
                            addressModel.setType(type);
                            addressModel.setHf_number(hf_number);
                            addressModel.setAddress(address);
                            addressModel.setLandmark(landmark);
                            addressModel.setState(state);
                            addressModel.setPincode(pincode);
                            addressModel.setCreated_at(created_at);
                            addressModel.setName(name);
                            addressModel.setMobile1(mobile1);
                            addressModel.setMobile2(mobile2);
                            addressModel.setDefault_address(defaults);

                            offerlist.add(addressModel);

                        }
                        if(offerlist.size()==0){
                            addressrecycle.setVisibility(View.GONE);
                        } else {
                            addressrecycle.setVisibility(View.VISIBLE);
                            bindAddressList(offerlist);
                        }
                    } else {
                        addressrecycle.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Address.this, "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Address.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);


    }

    private void bindAddressList(ArrayList<AddressModel> offerlist) {
        AddressAdapter addressAdapter = new AddressAdapter(offerlist, Address.this,this,this);
        addressrecycle.setLayoutManager(new LinearLayoutManager(Address.this, LinearLayoutManager.VERTICAL, false));
        addressrecycle.setAdapter(addressAdapter);
        addressAdapter.notifyDataSetChanged();

    }

    public void back(View view) {
        onBackPressed();
    }

    public void help(View view) {
        startActivity(new Intent(getApplicationContext(), Help.class));
    }

    public void addaddress(View view) {
        Intent intent=new Intent(getApplicationContext(), AddAdress.class);
        intent.putExtra("intenttype","add");
        startActivity(intent);
    }

    @Override
    public void getDefalt(String addressid) {
        if (Helper.INSTANCE.isNetworkAvailable(Address.this)){
            makeDefaultAddress(addressid);
        } else {
            Helper.INSTANCE.Error(Address.this, getString(R.string.NOCONN));
        }
    }

    private void makeDefaultAddress(String addressid) {
        String url = baseurl + "make_address_default";
        RequestQueue requestQueue = Volley.newRequestQueue(Address.this);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userid = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", userid);
        params.put("address_id", addressid);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        address();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Address.this, "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Address.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Helper.INSTANCE.isNetworkAvailable(Address.this)){
            address();
        } else {
            Helper.INSTANCE.Error(Address.this, getString(R.string.NOCONN));
        }
    }

    @Override
    public void addressListner(String id) {
        String url = baseurl + "user_address_delete";
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userid = yourPrefrence.getData("id");
        RequestQueue requestQueue = Volley.newRequestQueue(Address.this);
        Map<String, String> params = new HashMap();
        params.put("address_id", id);
        params.put("user_id", userid);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        address();
                        Toast.makeText(Address.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Address.this,""+error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

    }
}