package com.eminence.sitasrm.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eminence.sitasrm.Activity.OrderDetails;
import com.eminence.sitasrm.Activity.PointsWon;
import com.eminence.sitasrm.Activity.Scanner;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.myinnos.androidscratchcard.ScratchCard;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class Scratch_coupon extends AppCompatActivity {

    TextView txtDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_coupon);

        TextView point_value=findViewById(R.id.point_value);
        String point=getIntent().getExtras().getString("point");
        String id=getIntent().getExtras().getString("id");
        String redeem_status=getIntent().getExtras().getString("redeem_status");
        String date=getIntent().getExtras().getString("date");
        String time=getIntent().getExtras().getString("time");

        point_value.setText(point);
        ScratchCard mScratchCard = findViewById(R.id.scratchCard);
        txtDateTime = findViewById(R.id.txtDateTime);
         String dateFormet = "EARNED ON ";
        txtDateTime.setText(dateFormet + date);
        if (redeem_status.equalsIgnoreCase("1")) {
            mScratchCard.setVisibility(View.GONE);
        }
         mScratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                if (visiblePercent > 0.3) {
                    mScratchCard.setVisibility(View.GONE);
                     reedmcoupon(id, point);
                }
            }
        });
    }

    public void reedmcoupon(String couponid, String amount) {
        YourPreference yourPrefrence = YourPreference.getInstance(Scratch_coupon.this);
        String id = yourPrefrence.getData("id");
        String url = baseurl + "redeem_coupon";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(Scratch_coupon.this);
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("coupon_id", couponid);
        params.put("amount", amount);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("redeem_coupon response", "" + response);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        Intent intent=new Intent(Scratch_coupon.this,PointsWon.class);
                        intent.putExtra("point",amount);
                        intent.putExtra("type","scan");
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.i("redeem_coupon exception", "" + e);
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

    public void back(View view) {
        onBackPressed();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}