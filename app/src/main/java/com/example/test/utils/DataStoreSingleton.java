package com.example.test.utils;

import android.util.Log;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava2.RxDataStore;

import org.reactivestreams.Subscription;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataStoreSingleton {
    RxDataStore<Preferences> datastore;
    private static final DataStoreSingleton ourInstance = new DataStoreSingleton();
    public static DataStoreSingleton getInstance() {
        return ourInstance;
    }
    private DataStoreSingleton() { }
    public void setDataStore(RxDataStore<Preferences> datastore) {
        this.datastore = datastore;
    }
    public RxDataStore<Preferences> getDataStore() {
        return datastore;
    }

    public void saveValue(String keyName, String value) {
        if (datastore == null) return;

        Preferences.Key<String> key = new Preferences.Key<>(keyName);

        Disposable test = datastore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(key, value);
            return Single.just(mutablePreferences);
        }).subscribeOn(Schedulers.io()).subscribe(preferences -> {
            Log.d("SAVE DATA STORE SUCCESS", "sukses");
        }, throwable -> {
            Log.e("SAVE DATA STORE ERROR", throwable.toString());
        });
    }

    public void getValue(String keyName, GetValueCb cb) {
        if (datastore == null) return;

        Preferences.Key<String> key = new Preferences.Key<>(keyName);

        datastore.data().map(preferences -> preferences.get(key))
                .subscribeOn(Schedulers.io())
                .subscribe(new FlowableSubscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(String s) {
                        cb.onGetValue(s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface GetValueCb {
        void onGetValue(String s);
    }
}