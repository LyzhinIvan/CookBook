package com.cookbook.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeltaResponse {
    @JsonProperty("delta")
    public Double delta;
}
