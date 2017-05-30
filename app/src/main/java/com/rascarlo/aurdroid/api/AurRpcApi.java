package com.rascarlo.aurdroid.api;

import com.rascarlo.aurdroid.api.model.aur.info.AurInfoObject;
import com.rascarlo.aurdroid.api.model.aur.search.AurSearchObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AurRpcApi {

    // by name
    @GET("?v=5&type=search&by=name")
    Call<AurSearchObject> searchAurResultByName(@Query("arg") List<String> query);

    // by name-desc
    @GET("?v=5&type=search&by=name-desc")
    Call<AurSearchObject> searchAurResultByNameDesc(@Query("arg") List<String> query);

    // by maintainer
    @GET("?v=5&type=search&by=maintainer")
    Call<AurSearchObject> searchAurResultByMaintainer(@Query("arg") List<String> query);

    // info
    @GET("?v=5&type=info")
    Call<AurInfoObject> infoByPackageName(@Query("arg") String query);

}
