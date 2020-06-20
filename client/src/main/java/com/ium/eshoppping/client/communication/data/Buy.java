package com.ium.eshoppping.client.communication.data;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Buy implements Serializable
{

    @SerializedName("success")
    @Expose
    public Boolean success;

    public Buy withSuccess(Boolean success) {
        this.success = success;
        return this;
    }

}