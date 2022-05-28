package com.eminence.sitasrm.Activity.Profile;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;
import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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

public class AboutActivity extends AppCompatActivity {

    TextView about;
    ImageView image;
    LinearLayout bannerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        about=findViewById(R.id.about);
        image=findViewById(R.id.image);
        bannerLayout=findViewById(R.id.bannerLayout);

        if (Helper.INSTANCE.isNetworkAvailable(AboutActivity.this)){
            submit();
        } else {
            Helper.INSTANCE.Error(AboutActivity.this, getString(R.string.NOCONN));
        }
    }

    public void submit() {
        String url = baseurl + "content";
        RequestQueue requestQueue = Volley.newRequestQueue(AboutActivity.this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    bannerLayout.setVisibility(View.VISIBLE);
                    String aboutt = obj.getString("about");
                    String pp_image = obj.getString("abt_image");
                    Glide.with(getApplicationContext()).load(imagebaseurl+pp_image)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.loading)
                                    .centerCrop()
                                    .error(R.drawable.loading))
                            .into(image);
                    about.setText(Html.fromHtml(aboutt));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AboutActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AboutActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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