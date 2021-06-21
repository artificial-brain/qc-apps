package com.artificialbrain.quantumwheel.api;

import com.artificialbrain.quantumwheel.BuildConfig;
import com.artificialbrain.quantumwheel.models.QuantumRandomNumber;
import com.artificialbrain.quantumwheel.models.RandomNumberInput;
import com.artificialbrain.quantumwheel.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static RandomNumberAPI service;
    private static ApiManager apiManager;

    private ApiManager() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(20, TimeUnit.MINUTES);
        httpClient.connectTimeout(20, TimeUnit.MINUTES);
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(loggingInterceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(RandomNumberAPI.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void generateRandomNumber(RandomNumberInput randomNumberInput,
                                     Callback<QuantumRandomNumber> callback) {
        Call<QuantumRandomNumber> quantumRandomNumberCall = service.createUser(randomNumberInput);
        quantumRandomNumberCall.enqueue(callback);
    }
}
