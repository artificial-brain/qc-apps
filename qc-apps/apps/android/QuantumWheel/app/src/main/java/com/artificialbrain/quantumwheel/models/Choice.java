package com.artificialbrain.quantumwheel.models;

import java.io.Serializable;

public class Choice implements Serializable {

    public String choiceName;

    public Choice() {

    }

    public Choice(String choiceName) {
        this.choiceName = choiceName;
    }

    public String getChoiceName() {
        return choiceName;
    }

    public void setChoiceName(String choiceName) {
        this.choiceName = choiceName;
    }

}
