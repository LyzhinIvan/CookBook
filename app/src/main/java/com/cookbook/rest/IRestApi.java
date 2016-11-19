package com.cookbook.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRestApi {

    String UPDATE_URL = "update";
    String GET_UPDATE_SIZE_URL = "delta";

    @GET(UPDATE_URL)
    Call<UpdateResponse> update(@Query("lastUpdated") Long lastUpdate);

    @GET(GET_UPDATE_SIZE_URL)
    Call<DeltaResponse> getDelta(@Query("lastUpdated") Long lastUpdate);
}
