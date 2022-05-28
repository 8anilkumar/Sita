package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

public class ReferFriendActivity extends AppCompatActivity {

    ImageView imgBack;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String referral_by = yourPrefrence.getData("referral_by");

        msg = "Hey, Sita app is an application to get some rewards points. Doing shopping with STS Fragrances Pvt. Ltd.   You'll get Reward Points after scratch points are saved in your wallet and you can Redeem points from wallet.\n\n" +
                "Click here & install Sita app - https://play.google.com/store/apps/details?id=com.eminence.sitasrm&referrer="+referral_by+"\n\n Use my referral code:- "+referral_by;

    }

    public void help(View view) {
        startActivity(new Intent(getApplicationContext(), Help.class));
    }

    public void whatsapp(View view) {
        sendAppMsg("com.whatsapp");
    }

    public void facebook(View view) {
        sendAppMsg("com.facebook.katana");

    }

    public void message(View view) {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", msg);
        startActivity(intent);
    }

    public void more(View view) {
        shareText("Download Sita app ",msg);
    }


    public void sendAppMsg(String packagename) {

        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage(packagename);
            if (intent != null) {
                intent.putExtra(Intent.EXTRA_TEXT, msg);//
                startActivity(Intent.createChooser(intent, msg));
            } else {
                Toast.makeText(this, "App not found", Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "App not found", Toast.LENGTH_SHORT).show();
        }
    }


    public void shareText(String subject, String body) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent .setType("text/plain");
        txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(txtIntent ,"Share"));
    }
}