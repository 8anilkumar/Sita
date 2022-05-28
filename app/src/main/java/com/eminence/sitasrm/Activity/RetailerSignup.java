package com.eminence.sitasrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.eminence.sitasrm.Activity.Profile.RetailerLogin;
import com.eminence.sitasrm.Adapters.CityAdapter;
import com.eminence.sitasrm.Adapters.StateAdapter;
import com.eminence.sitasrm.Interface.CityListner;
import com.eminence.sitasrm.Interface.StateListner;
import com.eminence.sitasrm.Models.CityModel;
import com.eminence.sitasrm.Models.StateModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class RetailerSignup extends AppCompatActivity implements View.OnClickListener, StateListner, CityListner {

    EditText name, business_name, address_busnesnn, number;
    RelativeLayout otpverify_Layout;
    LinearLayout helpLayout;
    SpinnerDialog spinnerDialog;
    TextView languagebuttomn, txt_logIn, state_edt, city_edt;
    FirebaseAnalytics firebaseAnalytics;
    EditText gst;
    String token;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<StateModel> stateList = new ArrayList<>();
    ArrayList<CityModel> cityList = new ArrayList<>();
    ProgressBar mainProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_signup);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        business_name = findViewById(R.id.business_name);
        address_busnesnn = findViewById(R.id.address_busnesnn);
        state_edt = findViewById(R.id.state_edt);
        city_edt = findViewById(R.id.city_edt);
        mainProgressBar = findViewById(R.id.mainProgressBar);
        gst = findViewById(R.id.gst);
        initiate_views();

        list.add(getResources().getString(R.string.english));
        list.add(getResources().getString(R.string.hindi));
        list.add(getResources().getString(R.string.pangabi));
        list.add(getResources().getString(R.string.urdu));
        list.add(getResources().getString(R.string.bangali));

        getStateList();

        languagebuttomn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String language = yourPrefrence.getData("language");

        spinnerDialog = new SpinnerDialog(RetailerSignup.this, list, getResources().getString(R.string.select_language), R.style.DialogAnimations_SmileWindow, "Close");
        spinnerDialog.setCancellable(true);
        spinnerDialog.setShowKeyboard(false);

        if (language.equalsIgnoreCase("")) {
            languagebuttomn.setText("English");
        } else {
            if (language.equalsIgnoreCase("en")) {
                languagebuttomn.setText(getResources().getString(R.string.english));
            } else if (language.equalsIgnoreCase("hi")) {
                languagebuttomn.setText(getResources().getString(R.string.hindi));
            } else if (language.equalsIgnoreCase("pa")) {
                languagebuttomn.setText(getResources().getString(R.string.pangabi));
            } else if (language.equalsIgnoreCase("ur")) {
                languagebuttomn.setText(getResources().getString(R.string.urdu));
            } else if (language.equalsIgnoreCase("bn")) {
                languagebuttomn.setText(getResources().getString(R.string.bangali));
            } else {
                languagebuttomn.setText(getResources().getString(R.string.english));
            }
        }

        if (Helper.INSTANCE.isNetworkAvailable(RetailerSignup.this)) {
            languagechanger();
        } else {
            Helper.INSTANCE.Error(RetailerSignup.this, getString(R.string.NOCONN));
        }


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            token = task.getResult().getToken();
                        } else {
                            token = "";
                        }
                    }
                });

        state_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateList.size() != 0) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(RetailerSignup.this).create();
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
                    final AlertDialog alertDialog = new AlertDialog.Builder(RetailerSignup.this).create();
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
                    Toast.makeText(RetailerSignup.this, "First select your state", Toast.LENGTH_SHORT).show();
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

    private void initiate_views() {
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        otpverify_Layout = findViewById(R.id.otpverify_Layout);
        languagebuttomn = findViewById(R.id.languagebuttomn);
        txt_logIn = findViewById(R.id.txt_logIn);
        helpLayout = findViewById(R.id.helpLayout);
        otpverify_Layout.setOnClickListener(this);

        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetailerSignup.this, HelpappActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txt_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetailerSignup.this, RetailerLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Firebase_token_init(final String type) {
        if (token != null && !token.equalsIgnoreCase("")) {
            if (Helper.INSTANCE.isNetworkAvailable(RetailerSignup.this)) {
                user_signup_apicall(type);
            } else {
                Helper.INSTANCE.Error(RetailerSignup.this, getString(R.string.NOCONN));
            }
        } else {
            //Generating new token
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                token = task.getResult().getToken();
                                RetailerSignup.this.Firebase_token_init(type);
                            } else {
                                token = "";
                            }
                        }
                    });
        }
    }

    public void user_signup_apicall(final String type) {
        String url = baseurl + "retailer_signup";
        Map<String, String> params = new HashMap();
        RequestQueue requestQueue = Volley.newRequestQueue(RetailerSignup.this);
        params.put("mobile", number.getText().toString());
        params.put("name", name.getText().toString());
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");
                    Toast.makeText(RetailerSignup.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    if (status_code.equalsIgnoreCase("1")) {

                        if (type.equalsIgnoreCase("mobile")) {

                            Intent intent = new Intent(RetailerSignup.this, OTPScreen.class);
                            intent.putExtra("number", number.getText().toString());
                            intent.putExtra("name", name.getText().toString());
                            intent.putExtra("business_name", business_name.getText().toString());
                            intent.putExtra("address_busnesnn", address_busnesnn.getText().toString());
                            intent.putExtra("state_edt", state_edt.getText().toString());
                            intent.putExtra("city_edt", city_edt.getText().toString());
                            intent.putExtra("gst", gst.getText().toString());
                            intent.putExtra("type", "signup_retail");
                            startActivity(intent);
                        }
                    } else if (status_code.equalsIgnoreCase("100")) {
                        Toast.makeText(RetailerSignup.this, "Your Account has been Terminated!!", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences preferences = getSharedPreferences("YourCustomNamedPreference", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                finishAffinity();
                            }
                        }, 1500);
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
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.otpverify_Layout:
                if (name.getText().toString().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        name.setError("Field is required");
                    } else {
                        name.setError("फील्ड अनिवार्य है");
                    }
                } else if (business_name.getText().toString().trim().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        business_name.setError("Field is required");
                    } else {
                        business_name.setError("फील्ड अनिवार्य है");
                    }
                } else if (address_busnesnn.getText().toString().trim().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        address_busnesnn.setError("Field is required");
                    } else {
                        address_busnesnn.setError("फील्ड अनिवार्य है");
                    }
                } else if (state_edt.getText().toString().trim().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        state_edt.setError("Field is required");
                    } else {
                        state_edt.setError("फील्ड अनिवार्य है");
                    }
                } else if (city_edt.getText().toString().trim().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        city_edt.setError("Field is required");
                    } else {
                        city_edt.setError("फील्ड अनिवार्य है");
                    }
                } else if (number.getText().toString().length() != 10) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                        number.setError("Field is required");
                    } else {
                        number.setError("फील्ड अनिवार्य है");
                    }
                } else {
                    Firebase_token_init("mobile");
                }
        }
    }

    private void languagechanger() {
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                languagebuttomn.setText(item);

                if (position == 0) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("language", "en");
                    yourPrefrence.saveData("languagetext", item);
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                } else if (position == 1) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "hi");
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                } else if (position == 2) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "pa");
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                } else if (position == 3) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "ur");
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                } else if (position == 4) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "bn");
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }
            }
        });
    }

    public void back(View view) {
        onBackPressed();
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