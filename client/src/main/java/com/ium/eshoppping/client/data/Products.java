
package com.ium.eshoppping.client.data;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "products"
})
public class Products implements Serializable
{

    @JsonProperty("products")
    public List<Product> products = null;
    private final static long serialVersionUID = -8636444449947581726L;

    public Products withProducts(List<Product> products) {
        this.products = products;
        return this;
    }

}
