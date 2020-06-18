package com.ium.eshoppping.client.communication.data;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prediction implements Serializable
{

    @SerializedName("predicted_discount")
    @Expose
    public Integer predictedDiscount;
    private final static long serialVersionUID = -2099752986232784209L;

    public Prediction withPredictedDiscount(Integer predictedDiscount) {
        this.predictedDiscount = predictedDiscount;
        return this;
    }

}