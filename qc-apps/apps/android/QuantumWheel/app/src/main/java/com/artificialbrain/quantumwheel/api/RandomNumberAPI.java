package com.artificialbrain.quantumwheel.api;

import com.artificialbrain.quantumwheel.models.QuantumRandomNumber;
import com.artificialbrain.quantumwheel.models.RandomNumberInput;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RandomNumberAPI {
    @POST("/generateRandomNumber")
    Call<QuantumRandomNumber> createUser(@Body RandomNumberInput user);
}
