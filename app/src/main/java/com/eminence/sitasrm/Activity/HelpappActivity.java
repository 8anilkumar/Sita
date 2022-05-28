package com.eminence.sitasrm.Activity;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class HelpappActivity extends AppCompatActivity {

    EditText messegaA,subject;
    TextView counter;
    String Rqmobile="",InvalidMo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpapp);
        messegaA=findViewById(R.id.messegaA);
        subject=findViewById(R.id.subject);
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

    public void reqcalling(View view) {
        String mobile = subject.getText().toString();
        String message = messegaA.getText().toString();
        YourPreference yourPrefrence = YourPreference.getInstance(HelpappActivity.this);
        String language = yourPrefrence.getData("language");
        if (language.equalsIgnoreCase("hi")){
            Rqmobile = "मोबाइल नंबर चाहिए";
            InvalidMo="अमान्य मोबाइल नंबर";
        } else {
            Rqmobile = "Mobile number is required";
            InvalidMo="Invalid mobile number";
        }

        if(mobile.equalsIgnoreCase("")) {
            subject.setError(Rqmobile);
        } else  if(mobile.length() != 10){
            subject.setError(InvalidMo);
        } else  if(message.equalsIgnoreCase("")){
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                messegaA.setError("फील्ड अनिवार्य है");
            } else {
                messegaA.setError("Field is required");
            }

        } else {
            if (Helper.INSTANCE.isNetworkAvailable(HelpappActivity.this)){
                submit(mobile,message);
            } else {
                Helper.INSTANCE.Error(HelpappActivity.this, getString(R.string.NOCONN));
            }
        }
    }

    public void submit(String mobile, String message) {
        String url = baseurl + "help";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(HelpappActivity.this);
        Map<String, String> params = new HashMap();
        params.put("mobile",mobile);
        params.put("query",message);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        onBackPressed();
                        Toast.makeText(HelpappActivity.this, "We Will Contact you Soon", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HelpappActivity.this, ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(HelpappActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }
}