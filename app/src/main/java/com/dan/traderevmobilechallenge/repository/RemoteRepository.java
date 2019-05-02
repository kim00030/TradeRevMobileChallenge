package com.dan.traderevmobilechallenge.repository;

import com.dan.traderevmobilechallenge.BuildConfig;
import com.dan.traderevmobilechallenge.model.datasource.UnSplashApiDataSource;
import com.dan.traderevmobilechallenge.model.Photo;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Repository layer class to manage data source
 *
 * Created by Dan Kim on 2019-04-26
 */
public class RemoteRepository {

    public RemoteRepository() {
    }

    /**
     * Method to get photos
     * @return Observable that holds list of photos from unsplash API server
     */
    public Observable<ArrayList<Photo>> getAllPhotosFromUnsplashApi(int pageNo, int maxNumDataOnPage) {
        // Client ID is defined in BuildConfig for security purpose
        // it's stored in the BuildConfig while compilation time
        return UnSplashApiDataSource.getInstance().getAllPhotos(BuildConfig.clientId,pageNo,maxNumDataOnPage);
    }
}
