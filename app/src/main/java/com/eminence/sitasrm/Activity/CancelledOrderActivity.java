package com.eminence.sitasrm.Activity;

import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

public class CancelledOrderActivity extends AppCompatActivity {

    private TextView txt_orderId, txt_productName, txt_discount, txt_price;
    private ImageView product_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_order);

        txt_orderId = findViewById(R.id.txt_orderId);
        txt_productName = findViewById(R.id.txt_productName);
        txt_discount = findViewById(R.id.txt_discount);
        txt_price = findViewById(R.id.txt_price);
        product_image = findViewById(R.id.product_image);

        Intent intent = getIntent();
        String order_id = intent.getStringExtra("order_id");
        String product_id = intent.getStringExtra("product_id");
        String quantity = intent.getStringExtra("quantity");
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String productimage = intent.getStringExtra("product_image");

        YourPreference yourPrefrence = YourPreference.getInstance(CancelledOrderActivity.this);
        String language = yourPrefrence.getData("language");
        if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
            txt_orderId.setText("ऑर्डर आईडी: " + "ODSRM000" + order_id);
            txt_discount.setText("मात्रा: " + quantity);
        } else {
            txt_orderId.setText("Order Id: " + "ODSRM000" + order_id);
            txt_discount.setText("Quantity: " + quantity);
        }

        txt_productName.setText(name);
        txt_price.setText(price);

        Glide.with(CancelledOrderActivity.this).load(imagebaseurl + productimage)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo))
                .into(product_image);
    }

    public void back(View view) {
        onBackPressed();
    }
}