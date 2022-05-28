package com.eminence.sitasrm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eminence.sitasrm.MainActivity;
import com.eminence.sitasrm.R;

public class PaymentSuccessfullActivity extends AppCompatActivity {

    TextView dis_amount,idhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successfull);

        dis_amount=findViewById(R.id.dis_amount);
        idhome=findViewById(R.id.idhome);

        idhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentSuccessfullActivity.this, MainActivity.class);
                intent.putExtra("goto", "myorder");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}