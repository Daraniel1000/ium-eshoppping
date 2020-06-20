package com.ium.eshoppping.client.communication.data;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categories implements Serializable
{

    @SerializedName("categories")
    @Expose
    public List<Category> categories = null;
    private final static long serialVersionUID = -3332655977806056730L;

    @SerializedName("session")
    @Expose
    public Integer session;

    public Categories withSession(Integer session) {
        this.session = session;
        return this;
    }

    public Categories withCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

}