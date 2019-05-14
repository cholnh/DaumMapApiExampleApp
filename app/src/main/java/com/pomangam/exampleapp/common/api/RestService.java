package com.pomangam.exampleapp.common.api;

import com.pomangam.exampleapp.map.service.MapService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestService {

    private static final String BASE_URL = "https://www.pomangam.com:9530/api/v1/";

    private static class Singleton {
        private static final RestService instance = new RestService();
    }
    public static RestService getInstance() {
        return Singleton.instance;
    }

    private static final Retrofit restAdapter = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <T> T of(final Class<T> serviceClass) {
        return restAdapter.create(serviceClass);
    }

}
