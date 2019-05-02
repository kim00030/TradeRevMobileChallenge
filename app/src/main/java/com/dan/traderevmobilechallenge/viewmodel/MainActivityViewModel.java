package com.dan.traderevmobilechallenge.viewmodel;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dan.traderevmobilechallenge.components.NetworkStateChangeReceiver;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.repository.RemoteRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import static com.dan.traderevmobilechallenge.components.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

/**
 * ViewModel of {@link com.dan.traderevmobilechallenge.view.MainActivity}
 * It's being used by {@link com.dan.traderevmobilechallenge.view.fragments.GridViewFragment}
 * that is attached to the MainActivity.
 * <p>
 * It gets data sent by unsplash API server and make it as observable live data, then
 * GridViewFragment will be observing it
 * <p>
 * Created by Dan Kim on 2019-04-26
 */
public class MainActivityViewModel extends AndroidViewModel {

    private static final int PARAM_NUMBER_OF_PAGE = 10;
    private static final int PARAM_MAX_NUMBER_OF_PHOTOS_IN_A_PAGE = 30;
    private static final String TAG = "myDebug";
    private final RemoteRepository remoteRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<ArrayList<Photo>> photosLiveData = new MutableLiveData<>();
    private final  MutableLiveData<Boolean> networkStateLiveData = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        initNetWork();
        //Instantiate Repository
        remoteRepository = new RemoteRepository();
        getData();
    }

    /**
     * Method to initialize to receive broadcast message from application -level
     */
    private void initNetWork() {

        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                // if receiving network issue
                if (!isNetworkAvailable){
                    Log.d(TAG, "No NET WORK: ");
                    //Because of this set, MainActivity will be notifying the issue
                    networkStateLiveData.setValue(false);
                }else{

                    networkStateLiveData.setValue(true);
                    // Now network is backed up, check if no photos have not received yet
                    // call api to get them
                    if(photosLiveData.getValue() == null){
                        getData();
                    }
                }

            }
        }, intentFilter);
    }

    /**
     * Method to get Data from unsplash API server
     */
    private void getData() {
        // Add Observables that holds data from unsplash API on each page
        // Apparently up to 10 pages . I set to receive 30 photos on each page
        // By my observation, there is no single api to get all pages
        // Also, I checked manually , how many pages are available for me and up to 59 pages
        // are available and tested and ok, but wasteful, so I set only up tp 10 page I want to get
        List<Observable<ArrayList<Photo>>> photoObservableList = new ArrayList<>();
        for (int i = 1; i <= PARAM_NUMBER_OF_PAGE; i++) {

            photoObservableList.add(getObservablePhotos(i));
        }
        Observable.zip(photoObservableList, (Function<Object[], Object>) objects -> {

            ArrayList<Photo> tempPhotos = new ArrayList<>();
            for (Object object : objects) {
                //noinspection unchecked
                tempPhotos.addAll((Collection<? extends Photo>) object);

            }
            return tempPhotos;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "onNext: ");
                        //noinspection unchecked
                        photosLiveData.setValue((ArrayList<Photo>) o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }


    private Observable<ArrayList<Photo>> getObservablePhotos(int pageNo) {
        return remoteRepository.getAllPhotosFromUnsplashApi(pageNo, MainActivityViewModel.PARAM_MAX_NUMBER_OF_PHOTOS_IN_A_PAGE);
    }


    public MutableLiveData<ArrayList<Photo>> getPhotosLiveData() {
        return photosLiveData;
    }

    public MutableLiveData<Boolean> getNetworkStateLiveData() {
        return networkStateLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
