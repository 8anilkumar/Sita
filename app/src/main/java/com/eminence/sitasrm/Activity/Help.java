package com.eminence.sitasrm.Activity;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.eminence.sitasrm.MainActivity;
import com.eminence.sitasrm.Models.CartResponse;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Help extends AppCompatActivity {

    EditText messegaA;
    TextView counter;
    DatabaseHandler databaseHandler;
    List<CartResponse> alldata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setUpDB();

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
            if (Helper.INSTANCE.isNetworkAvailable(Help.this)){
                submit();
            } else {
                Helper.INSTANCE.Error(Help.this, getString(R.string.NOCONN));
            }
        }
    }

    public void submit() {
        String url = baseurl + "user_call_request";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(Help.this);
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
                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        String language = yourPrefrence.getData("language");
                        if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                            Toast.makeText(Help.this, "हम आपसे शीघ्र ही संपर्क करेंगे", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Help.this, "We Will Contact you Soon", Toast.LENGTH_SHORT).show();
                        }

                        Intent intent=new Intent(Help.this, MainActivity.class);
                        intent.putExtra("goto","home");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Help.this, ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(Help.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(Help.this, DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }
 }