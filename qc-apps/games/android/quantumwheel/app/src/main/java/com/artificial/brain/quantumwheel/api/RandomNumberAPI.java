package com.artificial.brain.quantumwheel.api;

import com.artificial.brain.quantumwheel.models.QuantumRandomNumber;
import com.artificial.brain.quantumwheel.models.RandomNumberInput;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RandomNumberAPI {
    @POST("/generateRandomNumber")
    Call<QuantumRandomNumber> createUser(@Body RandomNumberInput user);
}
