package com.eminence.sitasrm.Models;

public class ProductOrderModel {
    String productId,productName,productNameHindi,createdAt,quantity,price,productImage,odrStatus,product_caption,p_caption_hindi;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getOdrStatus() {
        return odrStatus;
    }

    public void setOdrStatus(String odrStatus) {
        this.odrStatus = odrStatus;
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

    public String getProductNameHindi() {
        return productNameHindi;
    }

    public void setProductNameHindi(String productNameHindi) {
        this.productNameHindi = productNameHindi;
    }
}
