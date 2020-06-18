
package com.ium.eshoppping.client.data;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "users"
})
public class Users implements Serializable
{

    @JsonProperty("users")
    public List<User> users = null;
    private final static long serialVersionUID = -1817023511322374755L;

    public Users withUsers(List<User> users) {
        this.users = users;
        return this;
    }

}
