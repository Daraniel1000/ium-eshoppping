package com.ium.eshoppping.client.data;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category implements Serializable
{

    @SerializedName("name")
    @Expose
    public String name;
    private final static long serialVersionUID = 3151866342126808476L;

    public Category withName(String name) {
        this.name = name;
        return this;
    }

}