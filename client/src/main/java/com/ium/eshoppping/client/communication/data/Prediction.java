package com.ium.eshoppping.client.communication.data;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prediction implements Serializable
{

    @SerializedName("prediction")
    @Expose
    public Double prediction;
    private final static long serialVersionUID = 7245946199961952777L;

    public Prediction withPrediction(Double prediction) {
        this.prediction = prediction;
        return this;
    }

    public String toString(){
        return prediction.toString();
    }

}