package com.example.test.presentation.ui.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.data.dto.ErrorDto;
import com.example.test.data.dto.auth.LoginDto;
import com.example.test.data.repository.AuthRepository;
import com.example.test.helper.constant.DatastoreConst;
import com.example.test.helper.utils.DataStoreSingleton;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getLoginSuccess() {
        return successMessage;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String email, String password) {
        AuthRepository repo = new AuthRepository();

        repo.login(new LoginDto.Body(email, password)).enqueue(new Callback<LoginDto.Response>() {
            @Override
            public void onResponse(Call<LoginDto.Response> call, Response<LoginDto.Response> response) {
                if (response.isSuccessful()) {
                    LoginDto.Response loginResponse = response.body();

                    DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
                    dataStoreSingleton.saveValue(DatastoreConst.ACC_TOKEN, loginResponse.token.accessToken);
                    dataStoreSingleton.saveValue(DatastoreConst.REF_TOKEN, loginResponse.token.refreshToken);
                    dataStoreSingleton.saveValue(DatastoreConst.USER_NAME, loginResponse.data.name);
                    dataStoreSingleton.saveValue(DatastoreConst.USER_EMAIL, loginResponse.data.email);
                    dataStoreSingleton.saveValue(DatastoreConst.USER_PHONE, loginResponse.data.phone);
                    dataStoreSingleton.saveValue(DatastoreConst.USER_AVATAR, loginResponse.data.avatar);

                    successMessage.postValue(loginResponse.message);
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();
                        Gson gson = new Gson();
                        ErrorDto errorResponse = gson.fromJson(errorBody, ErrorDto.class);

                        errorMessage.postValue(errorResponse.message);
                    } catch (Exception e) {
                        Log.e("UNKNOWN LOGIN ERROR", e.toString());
                        errorMessage.postValue("Login gagal");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginDto.Response> call, Throwable t) {
                Log.e("UNKNOWN LOGIN ERROR", t.toString());
                errorMessage.postValue("Login gagal");
            }
        });
    }
}
