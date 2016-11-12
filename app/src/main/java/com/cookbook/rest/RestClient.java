package com.cookbook.rest;

import android.content.Context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestClient {
    private static final String ENDPOINT = "https://cookbook-rest.herokuapp.com/api/";
    private static IRestApi sRestService = null;

    public static IRestApi getClient(Context context) {
        if (sRestService == null) {
            final OkHttpClient client = new OkHttpClient.Builder()
                    //.addInterceptor(new MockInterceptor())
                    .build();

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

            final Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(JacksonConverterFactory.create(mapper))
                    .baseUrl(ENDPOINT)
                    .client(client)
                    .build();

            sRestService = retrofit.create(IRestApi.class);
        }
        return sRestService;
    }

}
