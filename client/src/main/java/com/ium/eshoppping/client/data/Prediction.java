
package com.ium.eshoppping.client.data;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "prediction"
})
public class Prediction implements Serializable
{

    @JsonProperty("prediction")
    public Double prediction;
    private final static long serialVersionUID = 7245946199961952777L;

    public Prediction withPrediction(Double prediction) {
        this.prediction = prediction;
        return this;
    }

}
