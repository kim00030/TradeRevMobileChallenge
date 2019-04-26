package com.dan.traderevmobilechallenge.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.repository.RemoteRepository;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dan Kim on 2019-04-26
 */
public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = "myDebug";
    private RemoteRepository remoteRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        remoteRepository = new RemoteRepository();
        getData();
    }

    private void getData() {

        remoteRepository
                .getAllPhotosFromUnsplashApi()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Photo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Photo> photos) {
                        Log.d(TAG, "onNext: ");
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

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
