package com.ium.eshoppping.client.data;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Products implements Serializable
{

    @SerializedName("products")
    @Expose
    public List<Product> products = null;
    private final static long serialVersionUID = -8636444449947581726L;

    public Products withProducts(List<Product> products) {
        this.products = products;
        return this;
    }

}