package com.eminence.sitasrm.Activity.Profile;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

public class Contactus extends AppCompatActivity {

    EditText messegaA;
    TextView counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        messegaA=findViewById(R.id.messegaA);
        counter=findViewById(R.id.counter);
        messegaA.addTextChangedListener(new TextWatcher() {
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

    public void reqcalling(View view) {
        String sub = messegaA.getText().toString();
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String language = yourPrefrence.getData("language");

        if(sub.equalsIgnoreCase("")){
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                messegaA.setError("फील्ड अनिवार्य है");
            } else {
                messegaA.setError("Field is required");
            }

        }  else {
            if (Helper.INSTANCE.isNetworkAvailable(Contactus.this)){
                submit();
            } else {
                Helper.INSTANCE.Error(Contactus.this, getString(R.string.NOCONN));
            }
        }

    }

    public void submit() {
        String url = baseurl + "user_call_request";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(Contactus.this);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id",id);
        params.put("message",messegaA.getText().toString());
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        Toast.makeText(Contactus.this, "We Will Contact you Soon", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Contactus.this, MainActivity.class);
                        intent.putExtra("goto","home");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Contactus.this, ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Contactus.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }
}