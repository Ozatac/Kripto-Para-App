package com.tunahanozatac.javaretrofit.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tunahanozatac.javaretrofit.R;
import com.tunahanozatac.javaretrofit.adapter.RecyclerViewAdapter;
import com.tunahanozatac.javaretrofit.model.CryptoModel;
import com.tunahanozatac.javaretrofit.service.CryptoAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<CryptoModel> cryptoModels; //indirecegimiz data için array oluşturduk icinde CryptoModel olacak
    private String BASE_URL = "https://api.nomics.com/v1/";
    Retrofit retrofit;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        //Retrofit & Json
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        loadData();
    }

    private void loadData() {
        CryptoAPI cryptoAPI = retrofit.create(CryptoAPI.class);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(cryptoAPI.getData()
                .subscribeOn(Schedulers.io()) //Hangi thread'de kayıt olma işlemi yapılacağı
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handlerResponse));

        /*
        Call<List<CryptoModel>> call = cryptoAPI.getData();
        call.enqueue(new Callback<List<CryptoModel>>() {
            @Override
            public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                if (response.isSuccessful()) {
                    List<CryptoModel> responceList = response.body();
                    cryptoModels = new ArrayList<>(responceList); //Listeyi  arrayliste çevirecek
                    //RecyclerView
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    adapter = new RecyclerViewAdapter(cryptoModels);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<CryptoModel>> call, Throwable t) {
                //Hata oldugu zaman ne yapılacak
                t.printStackTrace();
            }
        });

         */
    }

    private void handlerResponse(List<CryptoModel> cryptoModelList) {

        cryptoModels = new ArrayList<>(cryptoModelList); //Listeyi  arrayliste çevirecek
        //RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new RecyclerViewAdapter(cryptoModels);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
