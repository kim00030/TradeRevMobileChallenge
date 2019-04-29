package com.dan.traderevmobilechallenge.remote;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Http client to communicate with unsplash api server
 *
 * Created by Dan Kim on 2019-04-26
 */
public class RetrofitClient {

    private static final String BASE_URL = "https://api.unsplash.com/";
    private static RetrofitClient instance;
    private final Retrofit retrofit;


    private RetrofitClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {

        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ServiceApi getServiceApi() {
        return retrofit.create(ServiceApi.class);
    }

}
