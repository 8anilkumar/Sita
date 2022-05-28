package com.eminence.sitasrm.Models;

public class ProductModel {
String product_id,single_description_hindi,single_description_english;
    String quantity,category_id,cart_availability,product_name,price,pouch_quantity,description,product_image,created_at,description_hindi,p_name_hindi,caption_eng,caption_hindi;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCart_availability() {
        return cart_availability;
    }

    public void setCart_availability(String cart_availability) {
        this.cart_availability = cart_availability;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getDescription_hindi() {
        return description_hindi;
    }

    public void setDescription_hindi(String description_hindi) {
        this.description_hindi = description_hindi;
    }

    public String getP_name_hindi() {
        return p_name_hindi;
    }

    public void setP_name_hindi(String p_name_hindi) {
        this.p_name_hindi = p_name_hindi;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPouch_quantity() {
        return pouch_quantity;
    }

    public void setPouch_quantity(String pouch_quantity) {
        this.pouch_quantity = pouch_quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCaption_eng() {
        return caption_eng;
    }

    public void setCaption_eng(String caption_eng) {
        this.caption_eng = caption_eng;
    }

    public String getCaption_hindi() {
        return caption_hindi;
    }

    public void setCaption_hindi(String caption_hindi) {
        this.caption_hindi = caption_hindi;
    }

    public String getSingle_description_hindi() {
        return single_description_hindi;
    }

    public void setSingle_description_hindi(String single_description_hindi) {
        this.single_description_hindi = single_description_hindi;
    }

    public String getSingle_description_english() {
        return single_description_english;
    }

    public void setSingle_description_english(String single_description_english) {
        this.single_description_english = single_description_english;
    }
}
