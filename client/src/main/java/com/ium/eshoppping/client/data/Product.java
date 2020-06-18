
package com.ium.eshoppping.client.data;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "category",
    "price",
    "product_id",
    "product_name"
})
public class Product implements Serializable
{

    @JsonProperty("category")
    public String category;
    @JsonProperty("price")
    public Double price;
    @JsonProperty("product_id")
    public Integer productId;
    @JsonProperty("product_name")
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

}
