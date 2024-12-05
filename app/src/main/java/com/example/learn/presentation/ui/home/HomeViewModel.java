package com.example.learn.presentation.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learn.data.dto.ErrorDto;
import com.example.learn.data.dto.auth.LogoutDto;
import com.example.learn.data.dto.resto.GetRestosDto;
import com.example.learn.domain.usecase.GetRestosUseCase;
import com.example.learn.domain.usecase.LogoutUseCase;
import com.example.learn.helper.constant.DatastoreConst;
import com.example.learn.helper.utils.DataStoreSingleton;
import com.example.learn.helper.utils.Resource;
import com.example.learn.presentation.ui.login.LoginActivity;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final GetRestosUseCase getRestosUseCase;
    private final LogoutUseCase logoutUseCase;
    private final MutableLiveData<String> logoutSuccessMsg = new MutableLiveData<>();
    private final MutableLiveData<String> logoutErrorMsg = new MutableLiveData<>();
    private final MutableLiveData<GetRestosDto.Query> query = new MutableLiveData<>(new GetRestosDto.Query());
    private final MutableLiveData<Resource<GetRestosDto.Response>> restosState = new MutableLiveData<>();

    @Inject
    public HomeViewModel(GetRestosUseCase getRestosUseCase, LogoutUseCase logoutUseCase) {
        this.getRestosUseCase = getRestosUseCase;
        this.logoutUseCase = logoutUseCase;
    }

    public LiveData<String> getLogoutSuccessMsg() {
        return logoutSuccessMsg;
    }

    public LiveData<String> getLogoutErrorMsg() {
        return logoutErrorMsg;
    }

    public LiveData<Resource<GetRestosDto.Response>> getRestosState() {
        if (restosState.getValue() == null) {
            fetchRestos();
        }
        return restosState;
    }

    public void logout() {
        DataStoreSingleton.getInstance().getValue(DatastoreConst.REF_TOKEN, s -> {
            logoutUseCase.execute(s, new Callback<LogoutDto.Response>() {
                @Override
                public void onResponse(Call<LogoutDto.Response> call, Response<LogoutDto.Response> response) {
                    if (response.isSuccessful()) {
                        LogoutDto.Response logoutResponse = response.body();

                        logoutSuccessMsg.postValue(logoutResponse.message);
                    } else {
                        try {
                            assert response.errorBody() != null;
                            String errorBody = response.errorBody().string();
                            ErrorDto errorResponse = new Gson().fromJson(errorBody, ErrorDto.class);
                            logoutErrorMsg.postValue(errorResponse.message);
                        } catch (Throwable e) {
                            Log.e("UNKNOWN LOGOUT ERROR", e.toString());
                            logoutErrorMsg.postValue("Logout failed");
                        }
                    }
                }

                @Override
                public void onFailure(Call<LogoutDto.Response> call, Throwable t) {
                    Log.e("UNKNOWN LOGOUT ERROR", t.toString());
                    logoutErrorMsg.postValue("Logout failed");
                }
            });
        });
    }

    public void fetchRestos() {
        restosState.setValue(Resource.loading());

        getRestosUseCase.execute(query.getValue(), new Callback<GetRestosDto.Response>() {
            @Override
            public void onResponse(Call<GetRestosDto.Response> call, Response<GetRestosDto.Response> response) {
                if (response.isSuccessful()) {
                    restosState.postValue(Resource.success(response.body()));
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();
                        ErrorDto errorResponse = new Gson().fromJson(errorBody, ErrorDto.class);
                        restosState.postValue(Resource.error(errorResponse.message));
                    } catch (Exception e) {
                        Log.e("UNKNOWN GET RESTO ERROR", e.toString());
                        restosState.postValue(Resource.error("Gagal mendapatkan data restoran"));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRestosDto.Response> call, Throwable t) {
                Log.e("UNKNOWN GET RESTO ERROR", t.toString());
                restosState.postValue(Resource.error("Gagal mendapatkan data restoran"));
            }
        });
    }
}
