package com.example.learn.presentation.ui.splash;

import androidx.lifecycle.ViewModel;

import com.example.learn.data.dto.auth.GetProfileDto;
import com.example.learn.domain.repository.AuthRepository;
import com.example.learn.helper.constant.DatastoreConst;
import com.example.learn.helper.utils.DataStoreSingleton;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class SplashViewModel extends ViewModel {
    public final AuthRepository authRepository;

    @Inject
    SplashViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void checkAuth(CheckAuthCb cb) {
        DataStoreSingleton.getInstance().getValue(DatastoreConst.ACC_TOKEN, s -> {
            if(s == null || s.isEmpty()) {
                cb.onGuest();
            } else {
                authRepository.getProfile().enqueue(new Callback<GetProfileDto>() {
                    @Override
                    public void onResponse(Call<GetProfileDto> call, Response<GetProfileDto> response) {
                        if(response.isSuccessful()) {
                            cb.onAuthenticated();
                        } else {
                            cb.onGuest();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetProfileDto> call, Throwable t) {
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
