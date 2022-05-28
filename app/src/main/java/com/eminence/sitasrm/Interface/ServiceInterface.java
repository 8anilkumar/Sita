package com.eminence.sitasrm.Interface;

import com.eminence.sitasrm.Models.SliderModel;
import com.eminence.sitasrm.Utils.Baseurl;

import retrofit2.Call;
import retrofit2.http.GET;

 public interface  ServiceInterface {

    @GET(Baseurl.RETAILERSLIDER)
    Call<SliderModel> getSliderdata();

    @GET(Baseurl.USERSLIDER)
    Call<SliderModel> getUserSliderdata();

}
