package com.example.learn.di;

import com.example.learn.data.api.AuthApi;
import com.example.learn.data.api.RestoApi;
import com.example.learn.data.api.RetrofitClient;
import com.example.learn.data.api.TrxApi;
import com.example.learn.data.repository.AuthRepositoryImpl;
import com.example.learn.data.repository.RestoRepositoryImpl;
import com.example.learn.data.repository.TrxRepositoryImpl;
import com.example.learn.domain.repository.AuthRepository;
import com.example.learn.domain.repository.RestoRepository;
import com.example.learn.domain.repository.TrxRepository;

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
