package com.eminence.sitasrm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

public class LanguageActivity extends AppCompatActivity {

    LinearLayout hindi_layout,english_layout,pangabi_layout,urdu_layout,bengali_layout;

    ImageView back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        hindi_layout=findViewById(R.id.hindi_layout);
        english_layout=findViewById(R.id.english_layout);
        pangabi_layout=findViewById(R.id.pangabi_layout);
        urdu_layout=findViewById(R.id.urdu_layout);
        back_btn=findViewById(R.id.back_btn);
        bengali_layout=findViewById(R.id.bengali_layout);
        String from=getIntent().getExtras().getString("from");
        if (from.equalsIgnoreCase("profile"))
        {
            back_btn.setVisibility(View.VISIBLE);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

         english_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                yourPrefrence.saveData("languagetext", "English (इंग्लिश)");
                yourPrefrence.saveData("language","en");
                Intent intent=new Intent(LanguageActivity.this,SplashScreen.class);
                startActivity(intent);
                finishAffinity();

            }
        });

         hindi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                yourPrefrence.saveData("language","hi");
                yourPrefrence.saveData("languagetext", "Hindi (हिंदी)");
                Intent intent=new Intent(LanguageActivity.this,SplashScreen.class);
                startActivity(intent);
                finishAffinity();

            }
        });


        pangabi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                yourPrefrence.saveData("language","pa");
                yourPrefrence.saveData("languagetext", "Panjabi (पंजाबी)");
                Intent intent=new Intent(LanguageActivity.this,SplashScreen.class);
                startActivity(intent);
                finishAffinity();

            }
        });


        urdu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                yourPrefrence.saveData("language","ur");
                yourPrefrence.saveData("languagetext", "Urdu (उर्दू)");
                Intent intent=new Intent(LanguageActivity.this,SplashScreen.class);
                startActivity(intent);
                finishAffinity();

            }
        });


        bengali_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                yourPrefrence.saveData("language","bn");
                yourPrefrence.saveData("languagetext", "Bangali (बंगाली)");
                Intent intent=new Intent(LanguageActivity.this,SplashScreen.class);
                startActivity(intent);
                finishAffinity();
            }
        });

    }

    public void back(View view) {
        onBackPressed();
    }
}