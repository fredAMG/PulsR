package com.example.fred_liu.pulsr;

import com.example.fred_liu.pulsr.models.RequestBody;
import com.example.fred_liu.pulsr.models.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("devices")
    Call<ResponseBody> registerDevice(@Body RequestBody body);
}
