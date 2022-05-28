package com.eminence.sitasrm.Activity.Profile;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    RatingBar ratingBar;
    EditText feedback;
    TextView counter,name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback=findViewById(R.id.feedback);
        ratingBar=findViewById(R.id.ratingBar);
        counter=findViewById(R.id.counter);
        name=findViewById(R.id.name);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String namee = yourPrefrence.getData("name");

        name.setText(namee);
        feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               counter.setText(""+charSequence.length()+"/1000");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void back(View view) {
       onBackPressed();
    }

    public void help(View view) {
        startActivity(new Intent(getApplicationContext(), Help.class));
    }

    public void sendnow(View view) {
        if (Helper.INSTANCE.isNetworkAvailable(Feedback.this)){

            YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
            String language = yourPrefrence.getData("language");

            String rating = ""+ratingBar.getRating();
            String feed = feedback.getText().toString();
            if(feed.equalsIgnoreCase("")){
                if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                    feedback.setError("फील्ड अनिवार्य है");
                } else {
                    feedback.setError("Field is required");
                }
            } else if(rating.equalsIgnoreCase("") || rating.equalsIgnoreCase("0.0")) {
                if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                    Toast.makeText(Feedback.this, "कृपया रेटिंग दें", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Feedback.this, "Please give rating", Toast.LENGTH_SHORT).show();
                }
            } else {
                submit();
            }
        } else {
            Helper.INSTANCE.Error(Feedback.this, getString(R.string.NOCONN));
        }
    }

    public void submit() {
        String url = baseurl + "user_app_feedback";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(Feedback.this);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id",id);
        params.put("star_rate",""+ratingBar.getRating());
        params.put("feedback", feedback.getText().toString());
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {

                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        String language = yourPrefrence.getData("language");
                        if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                            Toast.makeText(Feedback.this, "आपकी प्रतिक्रिया के लिए धन्यवाद", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Feedback.this, "Thanks for your Feedback", Toast.LENGTH_SHORT).show();
                        }

                        Intent intent=new Intent(Feedback.this, MainActivity.class);
                        intent.putExtra("goto","home");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Feedback.this, ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Feedback.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }
}