package com.eminence.sitasrm.Activity;

import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

public class PointsWon extends AppCompatActivity {

    TextView points,pointswon,copy;
    ImageView imagegift;
    String msg;
    RelativeLayout gotorewardpointLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_won);

        points=findViewById(R.id.points);
        gotorewardpointLayout=findViewById(R.id.gotorewardpointLayout);
        imagegift=findViewById(R.id.imagegift);
        pointswon=findViewById(R.id.pointswon);
        copy=findViewById(R.id.copy);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String referral_by = yourPrefrence.getData("referral_by");

        gotorewardpointLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RewardPointActivity.class));
                finish();
            }
        });

        msg = "Hey, Sita app is an application to get some rewards points. Doing shopping with STS Fragrances Pvt. Ltd.   You'll get a QR code after scanning that QR code some Reward Points are saved in your wallet and you can Redeem interesting offers from the application.\n" +
                "\n\n Click here & install SITA APP - " +
                 "https://play.google.com/store/apps/details?id=com.eminence.sitasrm&referrer="+referral_by+"\n\n *Use my referral code:-*"+referral_by;

        pointswon.setText(msg);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Text Copied", msg);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(PointsWon.this, "Copy to Clipboard", Toast.LENGTH_SHORT).show();

            }
        });

        String point=getIntent().getExtras().getString("point");
        String type=getIntent().getExtras().getString("type");
        if (type.equalsIgnoreCase("redeem")) {
            String imagee=getIntent().getExtras().getString("imagee");

            Glide.with(PointsWon.this).load(imagebaseurl +imagee)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.gift)
                            .error(R.drawable.gift))
                    .into(imagegift);

        }
        points.setText(point);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.paytm);
        mp.start();
    }

    public void back(View view) {
        onBackPressed();
        finish();
    }
}