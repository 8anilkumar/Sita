package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.eminence.sitasrm.R;

public class CancelOrderDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order_details);


    }

    public void back(View view) {
        onBackPressed();
    }
}