package com.example.learn.presentation.ui.splash;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.learn.data.dto.auth.GetProfileDto;
import com.example.learn.data.repository.AuthRepository;
import com.example.learn.helper.constant.DatastoreConst;
import com.example.learn.helper.utils.DataStoreSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashViewModel extends ViewModel {
    public void checkAuth(CheckAuthCb cb) {
        Log.d("DATASTORE DEBUG 5", DataStoreSingleton.getInstance() == null ? "null" : DataStoreSingleton.getInstance().toString());
        DataStoreSingleton.getInstance().getValue(DatastoreConst.ACC_TOKEN, s -> {
            Log.d("DATASTORE DEBUG 6", s == null ? "null" : s);
            if(s == null || s.equals("")) {
                Log.d("GUEST", "Token null");
                cb.onGuest();
            } else {
                AuthRepository repo = new AuthRepository();
                repo.getProfile().enqueue(new Callback<GetProfileDto>() {
                    @Override
                    public void onResponse(Call<GetProfileDto> call, Response<GetProfileDto> response) {
                        if(response.isSuccessful()) {
                            Log.d("AUTH", "success");
                            cb.onAuthenticated();
                        } else {
                            Log.d("GUEST", "401");
                            cb.onGuest();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetProfileDto> call, Throwable t) {
                        Log.d("GUEST", "error fetching");
                        cb.onGuest();
                    }
                });
            }
        });
    }

    public interface CheckAuthCb {
        void onAuthenticated();
        void onGuest();
    }
}
