package com.eminence.sitasrm.Activity.Profile;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eminence.sitasrm.Activity.Help;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class AddAdress extends AppCompatActivity {

    EditText houseno, address, landmark, state, pincode;
    LinearLayout onthertype, worktype, hometype;
    String type = "";
    TextView hometext, Worktext, OtherText,tvNote;
    ImageView homeicon, Workicon, OtherIcon;
    String intenttype;
    String id;
    CheckBox deafult_checkbox;
    SpinnerDialog spinnerDialog;
    String[] statearray = {"Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jammu and Kashmir",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttarakhand",
            "Uttar Pradesh",
            "West Bengal",
            "Andaman and Nicobar Islands",
            "Chandigarh",
            "Dadra and Nagar Haveli",
            "Daman and Diu",
            "Delhi",
            "Lakshadweep",
            "Puducherry"
    };

    EditText fullName,mobile1,mobile2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);
        houseno = findViewById(R.id.houseno);
        address = findViewById(R.id.address);
        landmark = findViewById(R.id.landmark);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pincode);
        onthertype = findViewById(R.id.onthertype);
        worktype = findViewById(R.id.worktype);
        hometype = findViewById(R.id.hometype);
        hometext = findViewById(R.id.hometext);
        Worktext = findViewById(R.id.Worktext);
        OtherText = findViewById(R.id.OtherText);
        homeicon = findViewById(R.id.homeicon);
        Workicon = findViewById(R.id.Workicon);
        OtherIcon = findViewById(R.id.OtherIcon);
        fullName = findViewById(R.id.fullName);
        mobile1 = findViewById(R.id.txt_phoneNumber1);
        mobile2 = findViewById(R.id.txt_phoneNumber2);
        deafult_checkbox = findViewById(R.id.deafult_checkbox);
        tvNote = findViewById(R.id.tvNote);

        intenttype = getIntent().getExtras().getString("intenttype");
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });

        spinnerDialog = new SpinnerDialog(AddAdress.this, new ArrayList(Arrays.asList(statearray)), "Select State", R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
        spinnerDialog.setCancellable(true);
        spinnerDialog.setShowKeyboard(false);

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                state.setText(item);
            }
        });

        if (intenttype.equalsIgnoreCase("edit")) {
            tvNote.setText("Update your address");
        }


        if (intenttype.equalsIgnoreCase("edit")) {
            String addresss = getIntent().getExtras().getString("address");
            id = getIntent().getExtras().getString("id");
            String housenoo = getIntent().getExtras().getString("houseno");
            String landmarkk = getIntent().getExtras().getString("landmark");
            String statee = getIntent().getExtras().getString("state");
            String pincodew = getIntent().getExtras().getString("pincode");
            String typew = getIntent().getExtras().getString("type");
            String name = getIntent().getExtras().getString("name");
            String mobile11 = getIntent().getExtras().getString("mobile1");
            String mobile22 = getIntent().getExtras().getString("mobile2");
            String default_add = getIntent().getExtras().getString("default");

            fullName.setText(name);
            mobile1.setText(mobile11);
            mobile2.setText(mobile22);
            if (default_add.equalsIgnoreCase("1")) {
                deafult_checkbox.setChecked(true);
            }

            houseno.setText(housenoo);
            address.setText(addresss);
            landmark.setText(landmarkk);
            state.setText(statee);
            pincode.setText(pincodew);

            if (typew.equalsIgnoreCase("Other")) {
                onthertype.setBackgroundResource(R.drawable.ractenglebg2);
                worktype.setBackgroundResource(R.drawable.bordergrey);
                hometype.setBackgroundResource(R.drawable.bordergrey);
                OtherText.setTextColor(getResources().getColor(R.color.white));
                Worktext.setTextColor(getResources().getColor(R.color.grey));
                hometext.setTextColor(getResources().getColor(R.color.grey));
                ImageViewCompat.setImageTintList(OtherIcon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.white)));
                ImageViewCompat.setImageTintList(Workicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                ImageViewCompat.setImageTintList(homeicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                type = "Other";
            } else if (typew.equalsIgnoreCase("Work")) {
                onthertype.setBackgroundResource(R.drawable.bordergrey);
                worktype.setBackgroundResource(R.drawable.ractenglebg2);
                hometype.setBackgroundResource(R.drawable.bordergrey);
                OtherText.setTextColor(getResources().getColor(R.color.grey));
                Worktext.setTextColor(getResources().getColor(R.color.white));
                hometext.setTextColor(getResources().getColor(R.color.grey));
                ImageViewCompat.setImageTintList(OtherIcon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                ImageViewCompat.setImageTintList(Workicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.white)));
                ImageViewCompat.setImageTintList(homeicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                type = "Work";
            } else {
                onthertype.setBackgroundResource(R.drawable.bordergrey);
                worktype.setBackgroundResource(R.drawable.bordergrey);
                hometype.setBackgroundResource(R.drawable.ractenglebg2);
                OtherText.setTextColor(getResources().getColor(R.color.grey));
                Worktext.setTextColor(getResources().getColor(R.color.grey));
                hometext.setTextColor(getResources().getColor(R.color.white));
                ImageViewCompat.setImageTintList(OtherIcon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                ImageViewCompat.setImageTintList(Workicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                ImageViewCompat.setImageTintList(homeicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.white)));
                type = "Home";
            }

        } else {
            onthertype.setBackgroundResource(R.drawable.bordergrey);
            worktype.setBackgroundResource(R.drawable.bordergrey);
            hometype.setBackgroundResource(R.drawable.ractenglebg2);
            OtherText.setTextColor(getResources().getColor(R.color.grey));
            Worktext.setTextColor(getResources().getColor(R.color.grey));
            hometext.setTextColor(getResources().getColor(R.color.white));
            ImageViewCompat.setImageTintList(OtherIcon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
            ImageViewCompat.setImageTintList(Workicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
            ImageViewCompat.setImageTintList(homeicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.white)));
            type = "Home";
        }

        onthertype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onthertype.setBackgroundResource(R.drawable.ractenglebg2);
                worktype.setBackgroundResource(R.drawable.bordergrey);
                hometype.setBackgroundResource(R.drawable.bordergrey);
                OtherText.setTextColor(getResources().getColor(R.color.white));
                Worktext.setTextColor(getResources().getColor(R.color.grey));
                hometext.setTextColor(getResources().getColor(R.color.grey));
                ImageViewCompat.setImageTintList(OtherIcon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.white)));
                ImageViewCompat.setImageTintList(Workicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                ImageViewCompat.setImageTintList(homeicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                type = "Other";
            }
        });
        worktype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onthertype.setBackgroundResource(R.drawable.bordergrey);
                worktype.setBackgroundResource(R.drawable.ractenglebg2);
                hometype.setBackgroundResource(R.drawable.bordergrey);
                OtherText.setTextColor(getResources().getColor(R.color.grey));
                Worktext.setTextColor(getResources().getColor(R.color.white));
                hometext.setTextColor(getResources().getColor(R.color.grey));
                ImageViewCompat.setImageTintList(OtherIcon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                ImageViewCompat.setImageTintList(Workicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.white)));
                ImageViewCompat.setImageTintList(homeicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                type = "Work";
            }
        });
        hometype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onthertype.setBackgroundResource(R.drawable.bordergrey);
                worktype.setBackgroundResource(R.drawable.bordergrey);
                hometype.setBackgroundResource(R.drawable.ractenglebg2);
                OtherText.setTextColor(getResources().getColor(R.color.grey));
                Worktext.setTextColor(getResources().getColor(R.color.grey));
                hometext.setTextColor(getResources().getColor(R.color.white));
                ImageViewCompat.setImageTintList(OtherIcon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                ImageViewCompat.setImageTintList(Workicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.grey)));
                ImageViewCompat.setImageTintList(homeicon, ColorStateList.valueOf(ContextCompat.getColor(AddAdress.this, R.color.white)));
                type = "Home";
            }
        });

    }

    public void submit() {
       String url = baseurl + "user_address_add";
       String default_string;
       if (deafult_checkbox.isChecked()) {
           default_string="1";
       } else {
           default_string="0";
       }

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String id = yourPrefrence.getData("id");

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(AddAdress.this);
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("type", type);
        params.put("name", fullName.getText().toString());
        params.put("mobile1", mobile1.getText().toString());
        params.put("mobile2", mobile2.getText().toString());
        params.put("defaults", default_string);
        params.put("hf_number", houseno.getText().toString());
        params.put("address", address.getText().toString());
        params.put("landmark", landmark.getText().toString());
        params.put("state", state.getText().toString());
        params.put("pincode", pincode.getText().toString());
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    Toast.makeText(AddAdress.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (r_code.equalsIgnoreCase("1")) {
                        onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(AddAdress.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

    }

    public void updateaddress() {

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userid = yourPrefrence.getData("id");

        String url = baseurl + "user_address_update";
        String default_string;
        if (deafult_checkbox.isChecked()) {
            default_string="1";
        } else {
            default_string="0";
        }

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(AddAdress.this);
        Map<String, String> params = new HashMap();
        params.put("address_id", id);
        params.put("type", type);
        params.put("name", fullName.getText().toString());
        params.put("mobile1", mobile1.getText().toString());
        params.put("mobile2", mobile2.getText().toString());
        params.put("defaults", default_string);
        params.put("hf_number", houseno.getText().toString());
        params.put("address", address.getText().toString());
        params.put("landmark", landmark.getText().toString());
        params.put("state", state.getText().toString());
        params.put("pincode", pincode.getText().toString());
        params.put("user_id", userid);

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    Toast.makeText(AddAdress.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (r_code.equalsIgnoreCase("1")) {
                        Intent intent = new Intent(AddAdress.this, Address.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddAdress.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

    }

    public void saveaddress(View view) {

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String language = yourPrefrence.getData("language");

        if (houseno.getText().toString().equalsIgnoreCase("")) {
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                houseno.setError("फील्ड अनिवार्य है");
            } else {
                houseno.setError("Field is required");
            }
        } else if (address.getText().toString().equalsIgnoreCase("")) {
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                address.setError("फील्ड अनिवार्य है");
            } else {
                address.setError("Field is required");
            }
        }  else if (fullName.getText().toString().equalsIgnoreCase("")) {
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                fullName.setError("फील्ड अनिवार्य है");
            } else {
                fullName.setError("Field is required");
            }
        } else if (mobile1.getText().toString().length() == 0) {
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                mobile1.setError("फील्ड अनिवार्य है");
            } else {
                mobile1.setError("Field is required");
            }
        } else if (mobile1.getText().toString().length() != 10) {
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                mobile1.setError("कृपया 10 अंकों का मोबाइल नंबर दर्ज करें");
            } else {
                mobile1.setError("Please enter 10 digit mobile number");
            }
        } else if (validateMobile()) {
            if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
                mobile2.setError("कृपया 10 अंकों का मोबाइल नंबर दर्ज करें");
            } else {
                mobile2.setError("Please enter 10 digit mobile number");
            }
        } else if (state.getText().toString().equalsIgnoreCase("")) {
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                state.setError("फील्ड अनिवार्य है");
            } else {
                state.setError("Field is required");
            }
        } else if (pincode.getText().toString().trim().length() == 0) {
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                pincode.setError("फील्ड अनिवार्य है");
            } else {
                pincode.setError("Field is required");
            }
        } else if (pincode.getText().toString().length() != 6) {
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                pincode.setError("पिन कोड अमान्य है");
            } else {
                pincode.setError("Invalid pin code");
            }
        } else if (type.equalsIgnoreCase("")) {
            if (language.equalsIgnoreCase("hi")||language.equalsIgnoreCase("")) {
                Toast.makeText(this, "पता का प्रकार चुनें", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Select Address Type", Toast.LENGTH_SHORT).show();
            }
        } else {
                if (Helper.INSTANCE.isNetworkAvailable(AddAdress.this)) {
                    if (intenttype.equalsIgnoreCase("edit")) {
                        updateaddress();
                    } else {
                        submit();
                    }
                } else {
                    Helper.INSTANCE.Error(AddAdress.this, getString(R.string.NOCONN));
                }
            }
    }

    public void back(View view) {
        onBackPressed();
    }

    public void help(View view) {
        startActivity(new Intent(getApplicationContext(), Help.class));
    }

    private boolean validateMobile() {
        boolean checkNumber = false;
        if(mobile2.getText().toString().length() != 0) {
           if (mobile2.getText().toString().length() != 10) {
              checkNumber = true;
           }
       }

        return checkNumber;
    }

}