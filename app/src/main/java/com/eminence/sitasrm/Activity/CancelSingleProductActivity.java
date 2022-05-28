package com.eminence.sitasrm.Activity;

import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

public class CancelSingleProductActivity extends AppCompatActivity {

    private TextView txt_orderId, txt_productName, txt_discount, txt_price, txt_createdAt;
    private ImageView product_image;
    LinearLayout helpLayout, cancelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_single_product);

        txt_orderId = findViewById(R.id.txt_orderId);
        txt_productName = findViewById(R.id.txt_productName);
        txt_discount = findViewById(R.id.txt_discount);
        txt_price = findViewById(R.id.txt_price);
        product_image = findViewById(R.id.product_image);
        helpLayout = findViewById(R.id.helpLayout);
        cancelLayout = findViewById(R.id.cancelLayout);
        txt_createdAt = findViewById(R.id.txt_createdAt);

        Intent intent = getIntent();
        String created_at = intent.getStringExtra("created_at");
        String order_id = intent.getStringExtra("order_id");
        String product_id = intent.getStringExtra("product_id");
        String quantity = intent.getStringExtra("quantity");
        String name = intent.getStringExtra("name");
        String name_hindi = intent.getStringExtra("name_hindi");
        String price = intent.getStringExtra("price");
        String productimage = intent.getStringExtra("product_image");

        txt_createdAt.setText(created_at);
        txt_price.setText(price);

        YourPreference yourPrefrence = YourPreference.getInstance(CancelSingleProductActivity.this);
        String language = yourPrefrence.getData("language");
        if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
            txt_orderId.setText("ऑर्डर आईडी: " + "ODSRM000" + order_id);
            txt_discount.setText("मात्रा: " + quantity);
            txt_productName.setText(name_hindi);
        } else {
            txt_orderId.setText("Order Id: " + "ODSRM000" + order_id);
            txt_discount.setText("Quantity: " + quantity);
            txt_productName.setText(name);
        }

        Glide.with(CancelSingleProductActivity.this).load(imagebaseurl + productimage)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo))
                .into(product_image);

        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CancelSingleProductActivity.this, Help.class);
                startActivity(intent);
            }
        });

        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CancelSingleProductActivity.this, RequestCancelActivity.class);
                intent.putExtra("order_id", order_id);
                intent.putExtra("product_id", product_id);
                intent.putExtra("cashback", "");
                intent.putExtra("orderDetails", "CancelSingleProduct");
                intent.putExtra("created_at", created_at);
                intent.putExtra("name", name);
                intent.putExtra("price", price);
                intent.putExtra("quantity", quantity);
                intent.putExtra("productimage", productimage);
                intent.putExtra("name_hindi", name_hindi);

                startActivity(intent);
            }
        });
    }

    public void back(View view) {
        onBackPressed();
    }


}