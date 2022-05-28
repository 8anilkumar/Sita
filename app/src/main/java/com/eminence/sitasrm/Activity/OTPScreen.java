package com.eminence.sitasrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eminence.sitasrm.MainActivity;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class OTPScreen extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout otpverify_Layout;
    LinearLayout resendOTPLayout;
    String token;
    EditText ed1, ed2, ed3, ed4;/*ed5,ed6*/
    String number, otp, name, type, otpNote;
    TextView numbertext, txt_timeCounter;
    ProgressBar mainProgressBar;
    InstallReferrerClient referrerClient;

    String refrer = "";
    String referrerUrl, business_name, address_busnesnn, state_edt, city_edt, gst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        intialize_views();

        try {
            number = getIntent().getExtras().getString("number");
            name = getIntent().getExtras().getString("name");
            type = getIntent().getExtras().getString("type");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (type.equalsIgnoreCase("signup_retail")) {
            business_name = getIntent().getExtras().getString("business_name");
            address_busnesnn = getIntent().getExtras().getString("address_busnesnn");
            state_edt = getIntent().getExtras().getString("state_edt");
            city_edt = getIntent().getExtras().getString("city_edt");
            gst = getIntent().getExtras().getString("gst");

        }

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String language = yourPrefrence.getData("language");
        refer();
        if (language.equalsIgnoreCase("hi")) {
            otpNote = "कृपया अपना मोबाइल नंबर जांचें " + number + " \nलॉग इन के लिए जारी रखें";
        } else {
            otpNote = "Please check your mobile number " + number + " \ncontinue to Login";
        }

        numbertext.setText(otpNote);
        setTimeCounter();

        // Get Firebase Token
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

        ed1.addTextChangedListener(new GenericTextWatcher(ed2, ed1));
        ed2.addTextChangedListener(new GenericTextWatcher(ed3, ed1));
        ed3.addTextChangedListener(new GenericTextWatcher(ed4, ed2));
        ed4.addTextChangedListener(new GenericTextWatcher(ed4, ed3));

    }

    private void setTimeCounter() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                txt_timeCounter.setText(millisUntilFinished / 1000 + " Second");
            }

            public void onFinish() {
                txt_timeCounter.setVisibility(View.GONE);
                resendOTPLayout.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    public class GenericTextWatcher implements TextWatcher {
        private EditText etPrev;
        private EditText etNext;

        public GenericTextWatcher(EditText etNext, EditText etPrev) {
            this.etPrev = etPrev;
            this.etNext = etNext;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if (text.length() == 1)
                etNext.requestFocus();
            else if (text.length() == 0)
                etPrev.requestFocus();
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }
    }


    private void intialize_views() {
        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        ed3 = findViewById(R.id.ed3);
        ed4 = findViewById(R.id.ed4);

        numbertext = findViewById(R.id.numbertext);
        otpverify_Layout = findViewById(R.id.otpverify_Layout);
        resendOTPLayout = findViewById(R.id.resendOTPLayout);
        txt_timeCounter = findViewById(R.id.txt_timeCounter);
        mainProgressBar = findViewById(R.id.mainProgressBar);

        otpverify_Layout.setOnClickListener(this);
        resendOTPLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.INSTANCE.isNetworkAvailable(OTPScreen.this)) {
                    resendOTP();

                } else {
                    Helper.INSTANCE.Error(OTPScreen.this, getString(R.string.NOCONN));
                }
            }
        });
    }

    private void Firebase_token_init(final String type) {

        if (token != null && !token.equalsIgnoreCase("")) {
            if (Helper.INSTANCE.isNetworkAvailable(OTPScreen.this)) {
                if (type.equalsIgnoreCase("signup_retail") || type.equalsIgnoreCase("login_retail")) {
                    otp_retail_verify();
                } else {
                    otpverify_apicall();
                }
            } else {
                Helper.INSTANCE.Error(OTPScreen.this, getString(R.string.NOCONN));
            }

        } else {
            //Generating new token

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                token = task.getResult().getToken();
                                Firebase_token_init(type);
                            } else {
                                token = "";
                            }
                        }
                    });
        }
    }

    private void otp_retail_verify() {
        String url = baseurl + "retailer_otp_verify";
        RequestQueue requestQueue = Volley.newRequestQueue(OTPScreen.this);
        if (referrerUrl.equalsIgnoreCase("utm_source=google-play&utm_medium=organic")) {
            referrerUrl = "";
        }

        Map<String, String> params = new HashMap();
        params.put("name", name);
        params.put("mobile", number);
        params.put("business_name", business_name);
        params.put("address", address_busnesnn);
        params.put("city", city_edt);
        params.put("gst_number", gst);
        params.put("otp", otp);
        params.put("device_id", token);
        params.put("state", state_edt);
        params.put("referral_by", referrerUrl);

        mainProgressBar.setVisibility(View.VISIBLE);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    mainProgressBar.setVisibility(View.GONE);
                    String r_code = obj.getString("status");
                    Toast.makeText(OTPScreen.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    if (r_code.equalsIgnoreCase("1")) {

                        String name = obj.getString("name");
                        String id = obj.getString("id");
                        String email = obj.getString("email");
                        String mobile = obj.getString("mobile");
                        String profile_photo = obj.getString("profile_photo");
                        String referral_by = obj.getString("referral_code");

                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        yourPrefrence.saveData("name", name);
                        yourPrefrence.saveData("id", id);
                        yourPrefrence.saveData("email", email);
                        yourPrefrence.saveData("mobile", mobile);
                        yourPrefrence.saveData("logintype", type);
                        yourPrefrence.saveData("referral_by", referral_by);
                        if ((type.equalsIgnoreCase("login")) || (type.equalsIgnoreCase("login_retail"))) {
                            Intent intent = new Intent(OTPScreen.this, MainActivity.class);
                            intent.putExtra("goto", "home");
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(OTPScreen.this, IntroSliderActivity.class);
                            intent.putExtra("type", type);
                            startActivity(intent);
                            finish();
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

    public void otpverify_apicall() {

        String url = baseurl + "user_otp_verify";
        RequestQueue requestQueue = Volley.newRequestQueue(OTPScreen.this);
        if (referrerUrl.equalsIgnoreCase("utm_source=google-play&utm_medium=organic")) {
            referrerUrl = "";
        }

        Map<String, String> params = new HashMap();
        params.put("name", name);
        params.put("mobile", number);
        params.put("otp", otp);
        params.put("device_id", token);
        params.put("state", "");
        params.put("referral_by", referrerUrl);
        mainProgressBar.setVisibility(View.VISIBLE);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    mainProgressBar.setVisibility(View.GONE);
                    String r_code = obj.getString("status");
                    Toast.makeText(OTPScreen.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                    if (r_code.equalsIgnoreCase("1")) {

                        String name = obj.getString("name");
                        String id = obj.getString("id");
                        String email = obj.getString("email");
                        String mobile = obj.getString("mobile");
                        String profile_photo = obj.getString("profile_photo");
                        String referral_by = obj.getString("referral_code");

                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        yourPrefrence.saveData("name", name);
                        yourPrefrence.saveData("id", id);
                        yourPrefrence.saveData("email", email);
                        yourPrefrence.saveData("mobile", mobile);
                        yourPrefrence.saveData("profile", profile_photo);
                        yourPrefrence.saveData("logintype", type);
                        yourPrefrence.saveData("referral_by", referral_by);

                        if ((type.equalsIgnoreCase("login")) || (type.equalsIgnoreCase("login_retail"))) {
                            Intent intent = new Intent(OTPScreen.this, MainActivity.class);
                            intent.putExtra("goto", "home");
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(OTPScreen.this, IntroSliderActivity.class);
                            intent.putExtra("type", type);
                            startActivity(intent);
                            finish();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.otpverify_Layout:
                otp = ed1.getText().toString() + ed2.getText().toString() + ed3.getText().toString() + ed4.getText().toString();
                if (otp.length() != 4) {
                    Toast.makeText(OTPScreen.this, "Required 4 Digit OTP", Toast.LENGTH_SHORT).show();
                } else {
                    Firebase_token_init(type);
                }
        }
    }

    public void resendOTP() {
        String url = baseurl + "user_resend_otp";
        RequestQueue requestQueue = Volley.newRequestQueue(OTPScreen.this);
        Map<String, String> params = new HashMap();
        params.put("mobile", number);
        JSONObject parameters = new JSONObject(params);
        mainProgressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    mainProgressBar.setVisibility(View.GONE);
                    resendOTPLayout.setVisibility(View.GONE);
                    txt_timeCounter.setVisibility(View.VISIBLE);

                    if (r_code.equalsIgnoreCase("1")) {
                        Toast.makeText(OTPScreen.this, "You can receive an otp via call or message", Toast.LENGTH_SHORT).show();
                        setTimeCounter();
                    } else {
                        Toast.makeText(OTPScreen.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
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
    }

    private void refer() {
        // on below line we are building our install referrer client and building it.
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                // this method is called when install referer setup is finished.
                switch (responseCode) {
                    // we are using switch case to check the response.
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // this case is called when the status is OK and
                        ReferrerDetails response = null;
                        try {
                            // on below line we are getting referrer details
                            // by calling get install referrer.
                            response = referrerClient.getInstallReferrer();

                            // on below line we are getting referrer url.
                            referrerUrl = response.getInstallReferrer();

                            // Toast.makeText(OTPScreen.this, ""+referrerUrl, Toast.LENGTH_SHORT).show();

                            // on below line we are getting referrer click time.
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();

                            // on below line we are getting app install time
                            long appInstallTime = response.getInstallBeginTimestampSeconds();

                            // on below line we are getting our time when
                            // user has used our apps instant experience.
                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                            // on below line we are getting our
                            // apps install referrer.
                            refrer = response.getInstallReferrer();

                            // on below line we are setting all detail to our text view.
                            String text = "Referrer is : \n" + referrerUrl + "\n" + "Referrer Click Time is : " + referrerClickTime + "\nApp Install Time : " + appInstallTime;

                        } catch (RemoteException e) {
                            // handling error case.
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        Toast.makeText(OTPScreen.this, "Feature not supported..", Toast.LENGTH_SHORT).show();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        Toast.makeText(OTPScreen.this, "Fail to establish connection", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Toast.makeText(OTPScreen.this, "Service disconnected..", Toast.LENGTH_SHORT).show();
            }
        });
    }

}