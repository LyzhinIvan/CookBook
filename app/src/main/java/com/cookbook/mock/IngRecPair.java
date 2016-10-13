package com.cookbook.mock;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IngRecPair {
    public long id, recId, ingId;
    public String quantity;

    @JsonCreator
    public IngRecPair(@JsonProperty("id") long id,@JsonProperty("recId") long recId,
                      @JsonProperty("ingId") long ingId,@JsonProperty("quantity") String quantity) {
        this.id = id;
        this.recId = recId;
        this.ingId = ingId;
        this.quantity = quantity;
    }
}
