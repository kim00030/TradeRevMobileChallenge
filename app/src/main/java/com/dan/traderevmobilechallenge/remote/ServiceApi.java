package com.dan.traderevmobilechallenge.remote;

import com.dan.traderevmobilechallenge.model.Photo;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Client API to get data from unsplash api server
 *
 * Created by Dan Kim on 2019-04-26
 */
public interface ServiceApi {

    @GET("photos")
    Observable<ArrayList<Photo>> getAllPhotos(
            @Query("client_id") String clientId,
            @Query("page") int page,
            @Query("per_page") int perPage);

}
