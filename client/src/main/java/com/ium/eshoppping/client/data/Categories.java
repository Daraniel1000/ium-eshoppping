
package com.ium.eshoppping.client.data;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "categories"
})
public class Categories implements Serializable
{

    @JsonProperty("categories")
    public List<Category> categories = null;
    private final static long serialVersionUID = -3332655977806056730L;

    public Categories withCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

}
