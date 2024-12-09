package com.example.learn.core.di;

import com.example.learn.features.auth.data.api.AuthApi;
import com.example.learn.features.resto.data.api.RestoApi;
import com.example.learn.core.api.RetrofitClient;
import com.example.learn.features.transactions.data.api.TrxApi;
import com.example.learn.features.auth.data.repository.AuthRepositoryImpl;
import com.example.learn.features.resto.data.repository.RestoRepositoryImpl;
import com.example.learn.features.transactions.data.repository.TrxRepositoryImpl;
import com.example.learn.features.auth.domain.repository.AuthRepository;
import com.example.learn.features.resto.domain.repository.RestoRepository;
import com.example.learn.features.transactions.domain.repository.TrxRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Provides
    @Singleton
    public RestoApi provideRestoApi() {
        return RetrofitClient.getClient().create(RestoApi.class);
    }

    @Provides
    @Singleton
    public RestoRepository provideRestoRepository(RestoApi restoApi) {
        return new RestoRepositoryImpl(restoApi);
    }

    @Provides
    @Singleton
    public AuthApi provideAuthApi() {
        return RetrofitClient.getClient().create(AuthApi.class);
    }

    @Provides
    @Singleton
    public AuthRepository provideAuthRepository(AuthApi authApi) {
        return new AuthRepositoryImpl(authApi);
    }

    @Provides
    @Singleton
    public TrxApi provideTrxApi() {
        return RetrofitClient.getClient().create(TrxApi.class);
    }

    @Provides
    @Singleton
    public TrxRepository provideTrxRepository(TrxApi trxApi) {
        return new TrxRepositoryImpl(trxApi);
    }
}
