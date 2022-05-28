package com.eminence.sitasrm.Activity;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.eminence.sitasrm.Adapters.CityAdapter;
import com.eminence.sitasrm.Adapters.StateAdapter;
import com.eminence.sitasrm.Interface.CityListner;
import com.eminence.sitasrm.Interface.StateListner;
import com.eminence.sitasrm.Models.CityModel;
import com.eminence.sitasrm.Models.StateModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateRetailerProfile extends AppCompatActivity implements StateListner ,CityListner{

    RelativeLayout submitLayout;
    EditText name, business_name, address_busnesnn, number,gst;
    TextView state_edt, city_edt;
    String userEmail="",userDOB="";
    ProgressBar mainProgressBar;
    ArrayList<StateModel> stateList = new ArrayList<>();
    ArrayList<CityModel> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_retailer_profile);
        submitLayout = findViewById(R.id.submitLayout);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        business_name = findViewById(R.id.business_name);
        address_busnesnn = findViewById(R.id.address_busnesnn);
        state_edt = findViewById(R.id.state_edt);
        city_edt = findViewById(R.id.city_edt);
        mainProgressBar = findViewById(R.id.mainProgressBar);
        gst = findViewById(R.id.gst);

        getUserProfile();
        getStateList();

        state_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateList.size() != 0) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(UpdateRetailerProfile.this).create();
                    LayoutInflater inflater = getLayoutInflater();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    View convertView = inflater.inflate(R.layout.state_dialog, null);
                    RecyclerView stateRecyclerView = convertView.findViewById(R.id.stateRecyclerView);
                    TextView txtNote = convertView.findViewById(R.id.txtNote);
                    txtNote.setText("Please Select your state");
                    setStateListData(stateRecyclerView, stateList, alertDialog);

                    alertDialog.setView(convertView);

                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    alertDialog.getWindow().setAttributes(layoutParams);
                    alertDialog.show();
                }
            }
        });

        city_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cityList.size() != 0) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(UpdateRetailerProfile.this).create();
                    LayoutInflater inflater = getLayoutInflater();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    View convertView = inflater.inflate(R.layout.state_dialog, null);
                    RecyclerView stateRecyclerView = convertView.findViewById(R.id.stateRecyclerView);
                    TextView txtNote = convertView.findViewById(R.id.txtNote);
                    txtNote.setText("Please Select your city");
                    setCityListData(stateRecyclerView, cityList, alertDialog);

                    alertDialog.setView(convertView);

                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    alertDialog.getWindow().setAttributes(layoutParams);
                    alertDialog.show();
                } else {
                    Toast.makeText(UpdateRetailerProfile.this, "First select your state", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        name.setError("Field is required");
                    } else {
                        name.setError("फील्ड अनिवार्य है");
                    }
                }
                else if (business_name.getText().toString().trim().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        business_name.setError("Field is required");
                    } else {
                        business_name.setError("फील्ड अनिवार्य है");
                    }
                }
                else if (address_busnesnn.getText().toString().trim().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        address_busnesnn.setError("Field is required");
                    } else {
                        address_busnesnn.setError("फील्ड अनिवार्य है");
                    }
                }
                else if (state_edt.getText().toString().trim().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        state_edt.setError("Field is required");
                    } else {
                        state_edt.setError("फील्ड अनिवार्य है");
                    }
                }
                else if (city_edt.getText().toString().trim().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        city_edt.setError("Field is required");
                    } else {
                        city_edt.setError("फील्ड अनिवार्य है");
                    }
                }
                else if (number.getText().toString().length() != 10) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        number.setError("Field is required");
                    } else {
                        number.setError("फील्ड अनिवार्य है");
                    }
                } else {
                        updateProfileDetails();
                }
            }
        });


    }

    private void setStateListData(RecyclerView stateRecyclerView, ArrayList<StateModel> stateList, AlertDialog alertDialog) {
        StateAdapter orderAdapter = new StateAdapter(stateList, this, this, alertDialog);
        stateRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        stateRecyclerView.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
    }

    private void setCityListData(RecyclerView stateRecyclerView, ArrayList<CityModel> cityList, AlertDialog alertDialog) {
        CityAdapter orderAdapter = new CityAdapter(cityList, this, this, alertDialog);
        stateRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        stateRecyclerView.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
    }

    public void back(View view) {
        onBackPressed();
    }

    private void updateProfileDetails() {
        String url = baseurl + "user_profile_update";
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String id = yourPrefrence.getData("id");
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateRetailerProfile.this);
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("name", name.getText().toString().trim());
        params.put("mobile", number.getText().toString().trim());
        params.put("business_name", business_name.getText().toString().trim());
        params.put("address", address_busnesnn.getText().toString().trim());
        params.put("state", state_edt.getText().toString().trim());
        params.put("city", city_edt.getText().toString().trim());
        params.put("gst_number", gst.getText().toString().trim());
        params.put("email",userEmail);
        params.put("birth_date",userDOB);

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        onBackPressed();
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

    public void getUserProfile() {
        String url = baseurl + "user_profile";
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateRetailerProfile.this);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
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
                            name.setText(jsonObject2.getString("name"));
                            business_name.setText(jsonObject2.getString("business_name"));
                            address_busnesnn.setText(jsonObject2.getString("address"));
                            state_edt.setText(jsonObject2.getString("state"));
                            city_edt.setText(jsonObject2.getString("city"));
                            gst.setText(jsonObject2.getString("gst_number"));
                            number.setText(jsonObject2.getString("mobile"));
                            number.setEnabled(false);
                            userEmail = jsonObject2.getString("email");
                            userDOB = jsonObject2.getString("birth_date");
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

    private void getStateList() {

        stateList.clear();
        String url = baseurl + "states";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        stateList.clear();
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            StateModel stateModel = new StateModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String stateId = jsonObject2.getString("id");
                            String stateName = jsonObject2.getString("name");

                            stateModel.setStateId(stateId);
                            stateModel.setStateName(stateName);
                            stateList.add(stateModel);
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

    private void getCityList(String stateId) {

        cityList.clear();
        String url = baseurl + "cities";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Map<String, String> params = new HashMap();
        params.put("state_id", stateId);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        cityList.clear();
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            CityModel stateModel = new CityModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String cityId = jsonObject2.getString("id");
                            String cityName = jsonObject2.getString("name");

                            stateModel.setCityId(cityId);
                            stateModel.setCityName(cityName);
                            cityList.add(stateModel);
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

    @Override
    public void stateListner(String stateId, String stateName) {
        state_edt.setText(stateName);
        getCityList(stateId);
    }

    @Override
    public void cityListner(String cityId, String cityName) {
        city_edt.setText(cityName);
    }

}