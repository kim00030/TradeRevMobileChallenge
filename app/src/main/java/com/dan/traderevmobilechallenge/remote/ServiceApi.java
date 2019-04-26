package com.dan.traderevmobilechallenge.remote;

import com.dan.traderevmobilechallenge.model.Photo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dan Kim on 2019-04-26
 */
public interface ServiceApi {

    @GET("photos")
    Observable<List<Photo>> getAllPhotos(@Query("client_id") String clientId);

}
