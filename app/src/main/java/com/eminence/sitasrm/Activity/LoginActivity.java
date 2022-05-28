package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText number;
    LinearLayout helpLayout;
    RelativeLayout otpverify_Layout;
    SpinnerDialog spinnerDialog;
    String language;
    TextView languagebuttomn, txt_signUp;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intialize_view();

        list.add(getResources().getString(R.string.english));
        list.add(getResources().getString(R.string.hindi));
        list.add(getResources().getString(R.string.pangabi));
        list.add(getResources().getString(R.string.urdu));
        list.add(getResources().getString(R.string.bangali));

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String language = yourPrefrence.getData("languagetext");

        spinnerDialog = new SpinnerDialog(LoginActivity.this, list, "Select Language", R.style.DialogAnimations_SmileWindow, "Close");
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false); // for open keyboard by default

        if (language.equalsIgnoreCase("")) {
            languagebuttomn.setText("English");
        } else {
            languagebuttomn.setText(language);
        }

        if (Helper.INSTANCE.isNetworkAvailable(LoginActivity.this)) {
            languagechanger();
        } else {
            Helper.INSTANCE.Error(LoginActivity.this, getString(R.string.NOCONN));
        }

        languagebuttomn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });

    }

    private void intialize_view() {
        number = findViewById(R.id.number);
        otpverify_Layout = findViewById(R.id.otpverify_Layout);
        languagebuttomn = findViewById(R.id.languagebuttomn);
        txt_signUp = findViewById(R.id.txt_signUp);
        helpLayout = findViewById(R.id.helpLayout);
        otpverify_Layout.setOnClickListener(this);

        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HelpappActivity.class);
                startActivity(intent);
            }
        });

        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void submit() {
        String url = baseurl + "user_login";
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        Map<String, String> params = new HashMap();
        params.put("mobile", number.getText().toString());
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    Toast.makeText(LoginActivity.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        String name = obj.getString("name");
                        Intent intent = new Intent(LoginActivity.this, OTPScreen.class);
                        intent.putExtra("number", number.getText().toString());
                        intent.putExtra("name", name);
                        intent.putExtra("type", "login");
                        startActivity(intent);

                    } else if (r_code.equalsIgnoreCase("100")) {
                        Toast.makeText(LoginActivity.this, "Your Account has been Terminated!!", Toast.LENGTH_LONG).show();
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
                if (number.getText().toString().length()!=10) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String languag = yourPrefrence.getData("language");
                    if (languag.equalsIgnoreCase("en") || languag.equalsIgnoreCase("")) {
                        number.setError("Field is required");
                    } else {
                        number.setError("फील्ड अनिवार्य है");
                    }
                } else {
                    if (Helper.INSTANCE.isNetworkAvailable(LoginActivity.this)) {
                        submit();
                    } else {
                        Helper.INSTANCE.Error(LoginActivity.this, getString(R.string.NOCONN));
                    }
                }
        }
    }


    private void languagechanger() {
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                language = item;
                languagebuttomn.setText(item);

                if (position == 0) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("language", "en");
                    yourPrefrence.saveData("languagetext", item);
                    Toast.makeText(LoginActivity.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                } else if (position == 1) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "hi");
                    Toast.makeText(LoginActivity.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }else if (position == 2) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "pa");
                    Toast.makeText(LoginActivity.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }else if (position == 3) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "ur");
                    Toast.makeText(LoginActivity.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }else if (position == 4) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "bn");
                    Toast.makeText(LoginActivity.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
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