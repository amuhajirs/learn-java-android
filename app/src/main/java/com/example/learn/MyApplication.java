package com.example.learn;

import android.app.Application;

import androidx.credentials.CredentialManager;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import com.example.learn.common.constant.DatastoreConst;
import com.example.learn.common.utils.DataStoreSingleton;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            RxDataStore<Preferences> dataStoreRX = new RxPreferenceDataStoreBuilder(this, DatastoreConst.DATASTORE_NAME).build();
            dataStoreSingleton.setDataStore(dataStoreRX);
        }
    }
}
