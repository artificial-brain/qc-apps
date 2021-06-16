package com.artificialbrain.quantumwheel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RandomNumberAPI {
    @POST("/generateRandomNumber")
    Call<QuantumRandomNumber> createUser(@Body RandomNumberInput user);
}
