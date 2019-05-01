package com.dan.traderevmobilechallenge.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
