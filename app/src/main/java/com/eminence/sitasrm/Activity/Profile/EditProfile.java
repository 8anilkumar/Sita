package com.eminence.sitasrm.Activity.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eminence.sitasrm.Activity.Help;
import com.eminence.sitasrm.MainActivity;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class EditProfile extends AppCompatActivity {
    LinearLayout updateprofileLayout;
    EditText txt_name,txt_email;
    TextView txt_mobile;
    Calendar myCalendar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String mobile = intent.getStringExtra("mobile");
        String email = intent.getStringExtra("email");

        txt_name = findViewById(R.id.txt_name);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_email = findViewById(R.id.txt_email);

        myCalendar = Calendar.getInstance();

        updateprofileLayout = findViewById(R.id.updateprofileLayout);

        if(name.equalsIgnoreCase("") || name.equalsIgnoreCase("null")){
        } else {
            txt_name.setText(name);
        }

        if(mobile.equalsIgnoreCase("") || mobile.equalsIgnoreCase("null")){
        } else {
            txt_mobile.setText(mobile);
        }

        if(email.equalsIgnoreCase("") || email.equalsIgnoreCase("null")) {

        } else {
            txt_email.setText(email);
        }

        updateprofileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txt_name.getText().toString();
                String mobile = txt_mobile.getText().toString();
                String email = txt_email.getText().toString();

                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                String language = yourPrefrence.getData("language");

                if(name.equalsIgnoreCase("") || name.equalsIgnoreCase("null")){
                    if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                        txt_name.setError("फील्ड अनिवार्य है");
                    } else {
                        txt_name.setError("Field is required");
                    }
                } else if(email.equalsIgnoreCase("") || email.equalsIgnoreCase("null")) {
                    if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
                        txt_email.setError("फील्ड अनिवार्य है");
                    } else {
                        txt_email.setError("Field is required");
                    }
                }
//                } else if(!email.matches(emailPattern)){
//                    if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
//                        txt_email.setError("कृपया वैलिड ईमेल दर्ज़ करें");
//                    } else {
//                        txt_email.setError("Please Enter Valid Email");
//                    }
//                } else
                {
                    if (Helper.INSTANCE.isNetworkAvailable(EditProfile.this)){
                        updateProfileDetails(name,mobile,email);
                    } else {
                        Helper.INSTANCE.Error(EditProfile.this, getString(R.string.NOCONN));
                    }
                }
            }
        });
    }


    private void updateProfileDetails(String name, String mobile, String email) {
        String url = baseurl + "user_profile_update";
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String id = yourPrefrence.getData("id");
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("name", name);
        params.put("email", email);
        params.put("mobile", mobile);
        params.put("business_name", "");
        params.put("address", "");
        params.put("state", "");
        params.put("city", "");
        params.put("gst_number", "");
        params.put("birth_date","");

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        yourPrefrence.saveData("name", name);
                        yourPrefrence.saveData("email", email);
                        yourPrefrence.saveData("mobile", mobile);

                        onBackPressed();
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

    public void help(View view) {
        startActivity(new Intent(getApplicationContext(), Help.class));
    }

    public void back(View view) {
        onBackPressed();
    }

}