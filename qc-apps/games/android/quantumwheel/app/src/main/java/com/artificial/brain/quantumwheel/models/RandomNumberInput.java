package com.artificial.brain.quantumwheel.models;

public class RandomNumberInput {

    private int length;
    private String provider;
    private String api;
    private String device;

    public RandomNumberInput(int length, String provider, String api, String device) {
        this.length = length;
        this.provider = provider;
        this.api = api;
        this.device = device;
    }
}
