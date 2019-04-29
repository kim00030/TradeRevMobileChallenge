package com.dan.traderevmobilechallenge.repository;

import com.dan.traderevmobilechallenge.BuildConfig;
import com.dan.traderevmobilechallenge.datasource.UnSplashApiDataSource;
import com.dan.traderevmobilechallenge.model.Photo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Repository layer class to manage data source
 *
 * Created by Dan Kim on 2019-04-26
 */
public class RemoteRepository {

    private final String clientId = BuildConfig.clientId;

    public RemoteRepository() {
    }

    /**
     * Method to get photos
     * @return Observable that holds list of photos from unsplash API server
     */
    public Observable<ArrayList<Photo>> getAllPhotosFromUnsplashApi() {
        return UnSplashApiDataSource.getInstance().getAllPhotos(this.clientId);
    }
}
