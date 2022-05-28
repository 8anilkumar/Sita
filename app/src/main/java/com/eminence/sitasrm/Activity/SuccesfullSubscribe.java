package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eminence.sitasrm.MainActivity;
import com.eminence.sitasrm.R;

public class SuccesfullSubscribe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succesfull_subscribe);
    }

    public void home(View view) {

        Intent intent = new Intent(SuccesfullSubscribe.this, MainActivity.class);
        intent.putExtra("goto", "home");
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {

    }
}