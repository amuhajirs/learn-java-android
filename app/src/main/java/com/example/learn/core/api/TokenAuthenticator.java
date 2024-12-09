package com.example.learn.core.api;

import com.example.learn.common.constant.ApiConst;
import com.example.learn.common.constant.DatastoreConst;
import com.example.learn.common.utils.DataStoreSingleton;
import com.example.learn.features.auth.data.api.AuthApi;
import com.example.learn.features.auth.data.dto.RefreshDto;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokenAuthenticator implements Authenticator {
    @Override
    public Request authenticate(Route route, Response response) {
        String newAccessToken = refreshAccessToken();

        return response.request().newBuilder()
                .header("Authorization", "Bearer " + newAccessToken)
                .build();
    }

    private String refreshAccessToken() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConst.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            String token = getStoredRefreshToken();
            AuthApi authService = retrofit.create(AuthApi.class);
            retrofit2.Response<RefreshDto.Response> refreshResponse = authService.refresh(new RefreshDto.Body(token)).execute();

            if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                String accessToken = refreshResponse.body().token.accessToken;
                String refreshToken = refreshResponse.body().token.refreshToken;

                saveTokens(accessToken, refreshToken);

                return accessToken;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if refresh fails
    }

    private String getStoredRefreshToken() {
        return DataStoreSingleton.getInstance().getValueSync(DatastoreConst.REF_TOKEN);
    }

    private void saveTokens(String accessToken, String refreshToken) {
        DataStoreSingleton.getInstance().saveValue(DatastoreConst.ACC_TOKEN, accessToken);
        DataStoreSingleton.getInstance().saveValue(DatastoreConst.REF_TOKEN, refreshToken);
    }
}
