package com.example.test;

import android.app.Application;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import com.example.test.helper.constant.DatastoreConst;
import com.example.test.helper.utils.DataStoreSingleton;

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
