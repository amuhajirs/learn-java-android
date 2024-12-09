package com.example.learn.features.auth.domain.usecase;

import android.content.Context;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.example.learn.BuildConfig;
import com.example.learn.common.constant.DatastoreConst;
import com.example.learn.common.utils.DataStoreSingleton;
import com.example.learn.shared.data.dto.ErrorDto;
import com.example.learn.features.auth.data.dto.GoogleLoginDto;
import com.example.learn.features.auth.domain.repository.AuthRepository;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleLoginUseCase {
    private final AuthRepository authRepository;

    @Inject
    public GoogleLoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(Context context, ExecuteCb cb) {
        CredentialManager credentialManager = CredentialManager.create(context);
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                .setAutoSelectEnabled(true)
                .setNonce(UUID.randomUUID().toString())
                .build();

        GetCredentialRequest getCredRequest = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(context, getCredRequest, new CancellationSignal(), Executors.newSingleThreadExecutor(), new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
            @Override
            public void onResult(GetCredentialResponse getCredentialResponse) {
                Credential credential = getCredentialResponse.getCredential();

                if(credential != null) {
                    if(credential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                        GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.getData());
                        authRepository.googleLogin(new GoogleLoginDto.Body(googleIdTokenCredential.getIdToken())).enqueue(new Callback<GoogleLoginDto.Response>() {
                            @Override
                            public void onResponse(Call<GoogleLoginDto.Response> call, Response<GoogleLoginDto.Response> response) {
                                if(response.isSuccessful()) {
                                    GoogleLoginDto.Response data = response.body();

                                    DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
                                    dataStoreSingleton.saveValue(DatastoreConst.ACC_TOKEN, data.token.accessToken);
                                    dataStoreSingleton.saveValue(DatastoreConst.REF_TOKEN, data.token.refreshToken);
                                    dataStoreSingleton.saveValue(DatastoreConst.USER_NAME, data.data.name);
                                    dataStoreSingleton.saveValue(DatastoreConst.USER_EMAIL, data.data.email);
                                    dataStoreSingleton.saveValue(DatastoreConst.USER_PHONE, data.data.phone);
                                    dataStoreSingleton.saveValue(DatastoreConst.USER_AVATAR, data.data.avatar);
                                    cb.onSuccess(data);
                                } else {
                                    try {
                                        assert response.errorBody() != null;
                                        String errorBody = response.errorBody().string();
                                        ErrorDto errorResponse = new Gson().fromJson(errorBody, ErrorDto.class);
                                        cb.onFailure(errorResponse.message);
                                    } catch (IOException e) {
                                        cb.onFailure(e.toString());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GoogleLoginDto.Response> call, Throwable t) {
                                cb.onFailure(t.toString());
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(@NonNull GetCredentialException e) {
                Log.d("GOGLE", e.toString());
            }
        });
    }

    public interface ExecuteCb {
        void onSuccess(GoogleLoginDto.Response response);
        void onFailure(String msg);
    }
}
