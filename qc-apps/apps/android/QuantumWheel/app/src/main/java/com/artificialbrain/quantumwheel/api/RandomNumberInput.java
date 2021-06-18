package com.artificialbrain.quantumwheel.api;

public class RandomNumberInput {
    public RandomNumberInput(int length, String api, String device) {
        this.length = length;
        this.api = api;
        this.device = device;
    }

    private int length;
    private String api;
    private String device;

    public void setApi(String api) {
        this.api = api;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
