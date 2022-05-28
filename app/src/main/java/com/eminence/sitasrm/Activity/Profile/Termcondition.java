package com.eminence.sitasrm.Activity.Profile;


import static com.eminence.sitasrm.Utils.Baseurl.baseurl;
import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;

import org.json.JSONObject;

public class Termcondition extends AppCompatActivity {

    TextView term;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termcondition);

        term=findViewById(R.id.term);
        image=findViewById(R.id.image);

        if (Helper.INSTANCE.isNetworkAvailable(Termcondition.this)){
            submit();
        } else {
            Helper.INSTANCE.Error(Termcondition.this, getString(R.string.NOCONN));
        }

    }

    public void submit() {
        String url = baseurl + "content";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(Termcondition.this);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String privacy_policy = obj.getString("term_condition");
                    String pp_image = obj.getString("tc_image");
                    Glide.with(getApplicationContext()).load(imagebaseurl+pp_image)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.loading)
                                    .centerCrop()
                                    .error(R.drawable.loading))
                            .into(image);

                    term.setText(Html.fromHtml(privacy_policy));

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Termcondition.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    public void back(View view) {
        onBackPressed();
    }
}