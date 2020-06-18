
package com.ium.eshoppping.client.data;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name"
})
public class Category implements Serializable
{

    @JsonProperty("name")
    public String name;
    private final static long serialVersionUID = 3151866342126808476L;

    public Category withName(String name) {
        this.name = name;
        return this;
    }

}
