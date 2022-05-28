package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.eminence.sitasrm.MainActivity;
import com.eminence.sitasrm.R;

public class CancellationConfirmedActivity extends AppCompatActivity {

    LinearLayout checkOutLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_confirmed);

        checkOutLayout = findViewById(R.id.checkOutLayout);
        checkOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CancellationConfirmedActivity.this, MainActivity.class);
                intent.putExtra("goto","myorder");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

}