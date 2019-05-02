package com.dan.traderevmobilechallenge.model.datasource;

import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.remote.RetrofitClient;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Data source to handle for calling to get data from Unsplash api server
 *
 * Created by Dan Kim on 2019-04-26
 */
@SuppressWarnings("SpellCheckingInspection")
public class UnSplashApiDataSource {
    private static final UnSplashApiDataSource ourInstance = new UnSplashApiDataSource();

    public static UnSplashApiDataSource getInstance() {
        return ourInstance;
    }

    private UnSplashApiDataSource() { }

    /**
     * Method to get photos from Unsplash api server
     *
     * @param clientId client ID as parameter required by unsplash APi call
     * @return Observable object that holds list of photos. It is observed by
     * {@link com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel}
     */
    public Observable<ArrayList<Photo>> getAllPhotos(final String clientId, int pageNo, int maxNumDataOnPage){
        return RetrofitClient.getInstance().getServiceApi().getAllPhotos(clientId,pageNo,maxNumDataOnPage);
    }
}
