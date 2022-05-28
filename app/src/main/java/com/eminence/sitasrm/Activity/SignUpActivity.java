package com.eminence.sitasrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
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
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout otpverify_Layout;
    LinearLayout helpLayout;
    EditText name, number;
    SpinnerDialog spinnerDialog;
    String language;
    TextView languagebuttomn,txt_logIn;
    FirebaseAnalytics firebaseAnalytics;
    String token;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initiate_views();
        list.add(getResources().getString(R.string.english));
        list.add(getResources().getString(R.string.hindi));
        list.add(getResources().getString(R.string.pangabi));
        list.add(getResources().getString(R.string.urdu));
        list.add(getResources().getString(R.string.bangali));


        languagebuttomn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String language = yourPrefrence.getData("languagetext");

        spinnerDialog = new SpinnerDialog(SignUpActivity.this, list, "Select Language", R.style.DialogAnimations_SmileWindow, "Close");
        spinnerDialog.setCancellable(true);
        spinnerDialog.setShowKeyboard(false);

        if (language.equalsIgnoreCase("")) {
            languagebuttomn.setText("English");
        } else {
            languagebuttomn.setText(language);

        }

        if (Helper.INSTANCE.isNetworkAvailable(SignUpActivity.this)){
           languagechanger();
        } else {
            Helper.INSTANCE.Error(SignUpActivity.this, getString(R.string.NOCONN));
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
                Intent intent = new Intent(SignUpActivity.this, HelpappActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txt_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void Firebase_token_init(final String type) {
        if (token != null && !token.equalsIgnoreCase("")) {
            if (Helper.INSTANCE.isNetworkAvailable(SignUpActivity.this)){
                user_signup_apicall(type);
            } else {
                Helper.INSTANCE.Error(SignUpActivity.this, getString(R.string.NOCONN));
            }
        } else {
            //Generating new token
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                token = task.getResult().getToken();
                                SignUpActivity.this.Firebase_token_init(type);
                            } else {
                                token = "";
                            }
                        }
                    });
        }
    }

    public void user_signup_apicall(final String type) {
        String url = baseurl + "user_signup";
        Map<String, String> params = new HashMap();
        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        params.put("mobile", number.getText().toString());
        params.put("name", name.getText().toString());
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                // Toast.makeText(HotelMain.this, ""+response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));

                    String status_code = obj.getString("status");
                    Toast.makeText(SignUpActivity.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    if (status_code.equalsIgnoreCase("1")) {

                        if (type.equalsIgnoreCase("mobile")) {
                            Intent intent = new Intent(SignUpActivity.this, OTPScreen.class);
                            intent.putExtra("number", number.getText().toString());
                            intent.putExtra("name", name.getText().toString());
                            intent.putExtra("type", "signup");
                            startActivity(intent);
                        }
                    } else if (status_code.equalsIgnoreCase("100")) {
                        Toast.makeText(SignUpActivity.this, "Your Account has been Terminated!!", Toast.LENGTH_LONG).show();
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
                    } else {
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.otpverify_Layout:
                if (name.getText().toString().equalsIgnoreCase("")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en")||language.equalsIgnoreCase("")) {
                        name.setError("Field is required");
                    } else {
                        name.setError("फील्ड अनिवार्य है");
                    }
                } else if (number.getText().toString().length()!=10) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String language = yourPrefrence.getData("language");
                    if (language.equalsIgnoreCase("en")||language.equalsIgnoreCase("")) {
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
                //   Toast.makeText(Loannewlead.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                language = item;
                languagebuttomn.setText(item);

                if (position == 0) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("language", "en");
                    yourPrefrence.saveData("languagetext", item);
                    Toast.makeText(SignUpActivity.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                } else if (position == 1) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "hi");
                    Toast.makeText(SignUpActivity.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }else if (position == 2) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "pa");
                    Toast.makeText(SignUpActivity.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }else if (position == 3) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "ur");
                    Toast.makeText(SignUpActivity.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }else if (position == 4) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "bn");
                    Toast.makeText(SignUpActivity.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }
            }
        });
    }

    public void back(View view) {
        onBackPressed();
    }
}