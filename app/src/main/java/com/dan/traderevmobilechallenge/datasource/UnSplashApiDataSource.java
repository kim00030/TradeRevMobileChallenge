package com.dan.traderevmobilechallenge.datasource;

import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.remote.RetrofitClient;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Dan Kim on 2019-04-26
 */
public class UnSplashApiDataSource {
    private static final UnSplashApiDataSource ourInstance = new UnSplashApiDataSource();

    public static UnSplashApiDataSource getInstance() {
        return ourInstance;
    }

    private UnSplashApiDataSource() { }

    public Observable<List<Photo>> getAllPhotos(final String clientId){
        return RetrofitClient.getInstance().getServiceApi().getAllPhotos(clientId);
    }
}
