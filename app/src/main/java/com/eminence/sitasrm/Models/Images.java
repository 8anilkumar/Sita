package com.eminence.sitasrm.Models;

import java.io.Serializable;

/**
 * Created by ${Ican} on 21/2/18.
 */

public class Images implements Serializable {
    String id;
    String image;
    String path;
    String product_id;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
