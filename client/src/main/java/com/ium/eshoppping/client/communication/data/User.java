package com.ium.eshoppping.client.communication.data;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable
{

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    private final static long serialVersionUID = -5851781878224059688L;

    public User withName(String name) {
        this.name = name;
        return this;
    }

    public User withUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String toString(){
        return name;
    }

}