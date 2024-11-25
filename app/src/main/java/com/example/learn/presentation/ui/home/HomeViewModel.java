package com.example.learn.presentation.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learn.data.dto.ErrorDto;
import com.example.learn.data.dto.auth.LogoutDto;
import com.example.learn.data.dto.resto.GetRestosDto;
import com.example.learn.domain.repository.AuthRepository;
import com.example.learn.domain.repository.RestoRepository;
import com.example.learn.helper.constant.DatastoreConst;
import com.example.learn.helper.utils.DataStoreSingleton;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final RestoRepository restoRepository;
    private final AuthRepository authRepository;
    private final MutableLiveData<String> logoutSuccessMsg = new MutableLiveData<>();
    private final MutableLiveData<String> logoutErrorMsg = new MutableLiveData<>();
    private final MutableLiveData<GetRestosDto.Response> restos = new MutableLiveData<>();
    private final MutableLiveData<String> getRestoErrorMsg = new MutableLiveData<>();

    @Inject
    public HomeViewModel(RestoRepository restoRepository, AuthRepository authRepository) {
        this.restoRepository = restoRepository;
        this.authRepository = authRepository;
    }

    public LiveData<String> getLogoutSuccessMsg() {
        return logoutSuccessMsg;
    }

    public LiveData<String> getLogoutErrorMsg() {
        return logoutErrorMsg;
    }

    public LiveData<GetRestosDto.Response> getRestos() {
        if(restos.getValue() == null) {
            fetchRestos();
        }
        return restos;
    }

    public void logout() {
        DataStoreSingleton.getInstance().getValue(DatastoreConst.REF_TOKEN, s -> {
            authRepository.logout(new LogoutDto.Body(s)).enqueue(new Callback<LogoutDto.Response>() {
                @Override
                public void onResponse(Call<LogoutDto.Response> call, Response<LogoutDto.Response> response) {
                    if(response.isSuccessful()) {
                        LogoutDto.Response logoutResponse = response.body();
                        DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();

                        dataStoreSingleton.saveValue(DatastoreConst.ACC_TOKEN, "");
                        dataStoreSingleton.saveValue(DatastoreConst.REF_TOKEN, "");
                        dataStoreSingleton.saveValue(DatastoreConst.USER_NAME, "");
                        dataStoreSingleton.saveValue(DatastoreConst.USER_EMAIL, "");
                        dataStoreSingleton.saveValue(DatastoreConst.USER_PHONE, "");
                        dataStoreSingleton.saveValue(DatastoreConst.USER_AVATAR, "");

                        logoutSuccessMsg.postValue(logoutResponse.message);
                    } else {
                        try {
                            assert response.errorBody() != null;
                            String errorBody = response.errorBody().string();
                            Gson gson = new Gson();
                            ErrorDto errorResponse = gson.fromJson(errorBody, ErrorDto.class);
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
                }
            });
        });
    }

    private void fetchRestos() {
        restoRepository.getRestos().enqueue(new Callback<GetRestosDto.Response>() {
            @Override
            public void onResponse(Call<GetRestosDto.Response> call, Response<GetRestosDto.Response> response) {
                if(response.isSuccessful()) {
                    GetRestosDto.Response getRestosRes = response.body();
                    restos.postValue(getRestosRes);
                    Log.d("RESTO", getRestosRes.data[0].data[0].name);
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();
                        Gson gson = new Gson();
                        ErrorDto errorResponse = gson.fromJson(errorBody, ErrorDto.class);

                        getRestoErrorMsg.postValue(errorResponse.message);
                    } catch (Exception e) {
                        Log.e("UNKNOWN GET RESTO ERROR", e.toString());
                        getRestoErrorMsg.postValue("Gagal mendapatkan data restoran");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRestosDto.Response> call, Throwable t) {
                Log.e("UNKNOWN GET RESTO ERROR", t.toString());
                getRestoErrorMsg.postValue("Gagal mendapatkan data restoran");
            }
        });
    }
}
