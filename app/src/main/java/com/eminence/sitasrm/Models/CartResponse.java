package com.eminence.sitasrm.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class CartResponse {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String product_id;
    private String qty;
    private String price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public CartResponse( String product_id, String qty, String price) {
        this.id = id;
        this.product_id = product_id;
        this.qty = qty;
        this.price = price;
    }
}
