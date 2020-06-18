package com.ium.eshoppping.client.data;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Users implements Serializable
{

    @SerializedName("users")
    @Expose
    public List<User> users = null;
    private final static long serialVersionUID = -1817023511322374755L;

    public Users withUsers(List<User> users) {
        this.users = users;
        return this;
    }

}