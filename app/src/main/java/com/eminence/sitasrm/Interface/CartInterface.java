package com.eminence.sitasrm.Interface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.eminence.sitasrm.Models.CartResponse;

import java.util.List;

@Dao
public interface CartInterface {
    @Query("SELECT * FROM cart")
    List<CartResponse> getallcartdata();
    @Insert
    void addcart(CartResponse cartResponse);


    @Query("UPDATE cart SET qty = :qty WHERE product_id = :product_id")
    void setQty(String product_id,String qty);

    @Query("DELETE FROM cart")
    void deleteall();


    @Query("DELETE FROM cart WHERE product_id= :id ")
    void deletebyid(String id);


    @Query("SELECT * FROM cart WHERE product_id= :id ")
    boolean isproductavailable(String id);


    @Query("SELECT qty FROM cart WHERE product_id= :id ")
    String getqty(String id);

}
