package com.ium.eshoppping.client.communication.data;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product implements Serializable {

    @SerializedName("category")
    @Expose
    public String category;
    @SerializedName("price")
    @Expose
    public Double price;
    @SerializedName("product_id")
    @Expose
    public Integer productId;
    @SerializedName("product_name")
    @Expose
    public String productName;
    private final static long serialVersionUID = 1562816085536459971L;

    public Product withCategory(String category) {
        this.category = category;
        return this;
    }

    public Product withPrice(Double price) {
        this.price = price;
        return this;
    }

    public Product withProductId(Integer productId) {
        this.productId = productId;
        return this;
    }

    public Product withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String toString(){
        return productName;
    }

}