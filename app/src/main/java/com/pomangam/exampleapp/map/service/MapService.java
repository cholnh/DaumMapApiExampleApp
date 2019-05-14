package com.pomangam.exampleapp.map.service;

import com.pomangam.exampleapp.map.domain.MapDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface MapService {
    @GET("tests/maps/{employeeIdx}")
    Call<MapDto> getBy(@Path("employeeIdx") Integer employeeIdx);

    @PATCH("tests/maps/{employeeIdx}")
    Call<MapDto> patch(@Path("employeeIdx") Integer employeeIdx,
                       @Body MapDto mapDto);
}
