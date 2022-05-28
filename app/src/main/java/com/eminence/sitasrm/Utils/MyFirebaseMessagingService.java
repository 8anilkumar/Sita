package com.eminence.sitasrm.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.eminence.sitasrm.Activity.OrderDetails;
import com.eminence.sitasrm.Activity.RewardPointActivity;
import com.eminence.sitasrm.Activity.SplashScreen;
import com.eminence.sitasrm.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String badge;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        PendingIntent pendingIntent = null;
       //  Log.i("notificationsss","hewkkh");
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());

        //get data
        Map<String, String> data = remoteMessage.getData();
         String type = data.get("type").toString();
        String title = data.get("title").toString();
        String body = data.get("body").toString();
        String id = data.get("o_id").toString();
        int num = (int) System.currentTimeMillis();

        if (type.equalsIgnoreCase("scratch_card")||type.equalsIgnoreCase("scratch_card_redeem"))
        {
            Intent intent1 = new Intent(this, RewardPointActivity.class);
            //intent1.putExtra("order_id",o_id);
             intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this,num , intent1, PendingIntent.FLAG_ONE_SHOT);
        }else if (type.equalsIgnoreCase("place_order"))
        {
            Intent intent1 = new Intent(this, OrderDetails.class);
            //intent1.putExtra("order_id",o_id);
            intent1.putExtra("order_id",id);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, num, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        }else
        {
            Intent intent1 = new Intent(this, SplashScreen.class);
            //intent1.putExtra("order_id",o_id);
             intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, num, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        }

        badge=yourPrefrence.getData("badge");

        if (badge.equalsIgnoreCase("")) {
            badge="0";
        }

        int plusbadge=Integer.parseInt(badge)+1;
        yourPrefrence.saveData("badge",String.valueOf(plusbadge));
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(num, builder.build());
    }
}