package com.eminence.sitasrm.Models;

public class CartModel {
    String id,product_id,product_name,product_name_hindi,product_caption,
            p_caption_hindi,product_image,quantity,price,created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_name_hindi() {
        return product_name_hindi;
    }

    public void setProduct_name_hindi(String product_name_hindi) {
        this.product_name_hindi = product_name_hindi;
    }

    public String getProduct_caption() {
        return product_caption;
    }

    public void setProduct_caption(String product_caption) {
        this.product_caption = product_caption;
    }

    public String getP_caption_hindi() {
        return p_caption_hindi;
    }

    public void setP_caption_hindi(String p_caption_hindi) {
        this.p_caption_hindi = p_caption_hindi;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
