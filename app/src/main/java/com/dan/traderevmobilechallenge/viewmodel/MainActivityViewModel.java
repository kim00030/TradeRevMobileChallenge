package com.dan.traderevmobilechallenge.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.repository.RemoteRepository;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * ViewModel of {@link com.dan.traderevmobilechallenge.view.MainActivity}
 * It's being used by {@link com.dan.traderevmobilechallenge.view.fragments.GridViewFragment}
 * that is attached to the MainActivity.
 *
 * It gets data sent by unsplash API server and make it as observable live data, then
 * GridViewFragment will be observing it
 *
 * Created by Dan Kim on 2019-04-26
 */
public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = "myDebug";
    private final RemoteRepository remoteRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<ArrayList<Photo>> photosLiveData = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        //Instantiate Repository
        remoteRepository = new RemoteRepository();
        getData();
    }

    /**
     * Method to get Data from unsplash API server
     */
    private void getData() {

        remoteRepository
                .getAllPhotosFromUnsplashApi()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Photo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ArrayList<Photo> photos) {
                        // set list of photos into livedata so GridView will be updating
                        // its UIS with the data
                        photosLiveData.setValue(photos);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public MutableLiveData<ArrayList<Photo>> getPhotosLiveData() {
        return photosLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
