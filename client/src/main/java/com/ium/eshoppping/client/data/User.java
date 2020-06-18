
package com.ium.eshoppping.client.data;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "user_id"
})
public class User implements Serializable
{

    @JsonProperty("name")
    public String name;
    @JsonProperty("user_id")
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

}
