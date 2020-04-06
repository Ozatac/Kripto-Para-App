package com.tunahanozatac.javaretrofit.service;

import com.tunahanozatac.javaretrofit.model.CryptoModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface CryptoAPI {

    //Get: Veriyi almak için
    //Post: Sunucuya veri yazmak için

    // URL BASE -> WWWW.WEBSİTE.COM
    // Get -> price?key=xxx
    @GET("prices?key=5fc27e557b27b00d0bc6ac196711dc19")
    Observable<List<CryptoModel>> getData();

    //Call<List<CryptoModel>> getData();



}
