package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eminence.sitasrm.Activity.Profile.RetailerLogin;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class WhoeareYou extends AppCompatActivity {

    TextView customer,retailer;
    LinearLayout helpLayout;
    SpinnerDialog spinnerDialog;
    String language;
    TextView languagebuttomn;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whoeare_you);

        customer=findViewById(R.id.customer);
        retailer=findViewById(R.id.retailer);
        helpLayout=findViewById(R.id.helpLayout);
        languagebuttomn=findViewById(R.id.languagebuttomn);

        list.add(getResources().getString(R.string.english));
        list.add(getResources().getString(R.string.hindi));
        list.add(getResources().getString(R.string.pangabi));
        list.add(getResources().getString(R.string.urdu));
        list.add(getResources().getString(R.string.bangali));

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String language = yourPrefrence.getData("languagetext");

        spinnerDialog = new SpinnerDialog(WhoeareYou.this, list, "Select Language", R.style.DialogAnimations_SmileWindow, "Close");
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false); // for open keyboard by default

        if (language.equalsIgnoreCase("")) {
            languagebuttomn.setText("English");
        } else {
            languagebuttomn.setText(language);
        }

        if (Helper.INSTANCE.isNetworkAvailable(WhoeareYou.this)) {
            languagechanger();
        } else {
            Helper.INSTANCE.Error(WhoeareYou.this, getString(R.string.NOCONN));
        }

        languagebuttomn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });


        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OnBordingScreen.class));
            }
        });


        retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RetailerLogin.class));
            }
        });

        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WhoeareYou.this, HelpappActivity.class);
                startActivity(intent);
            }
        });

    }

    private void languagechanger() {
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                language = item;
                languagebuttomn.setText(item);

                if (position == 0) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("language", "en");
                    yourPrefrence.saveData("languagetext", item);
                    Toast.makeText(WhoeareYou.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                } else if (position == 1) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "hi");
                    Toast.makeText(WhoeareYou.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }else if (position == 2) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "pa");
                    Toast.makeText(WhoeareYou.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }else if (position == 3) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "ur");
                    Toast.makeText(WhoeareYou.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }else if (position == 4) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("languagetext", item);
                    yourPrefrence.saveData("language", "bn");
                    Toast.makeText(WhoeareYou.this, "Selected Language is " + item, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    finishAffinity();
                }
            }
        });
    }

}