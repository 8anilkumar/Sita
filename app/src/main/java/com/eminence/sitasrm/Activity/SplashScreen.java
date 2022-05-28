package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import com.eminence.sitasrm.MainActivity;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

import java.util.Locale;

import io.sentry.Sentry;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String language = yourPrefrence.getData("language");
        Sentry.captureMessage("testing SDK setup");

        if (language != null || !language.equalsIgnoreCase("")) {
            updateResources(SplashScreen.this, language);
        } else {
            updateResources(SplashScreen.this, "en");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intentServiceFire();

            }
        }, 2000);

    }

    private static boolean updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }

    private void intentServiceFire() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String id = yourPrefrence.getData("id");
        String language = yourPrefrence.getData("language");

        if (language.equalsIgnoreCase("")) {
            Intent intent=new Intent(getApplicationContext(), LanguageActivity.class);
            intent.putExtra("from","splash");
            startActivity(intent);
            finish();
        } else if (id.equalsIgnoreCase("") || id.equalsIgnoreCase(null)) {
            startActivity(new Intent(getApplicationContext(), WhoeareYou.class));
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("goto", "home");
            startActivity(intent);
            finish();
        }
    }
}