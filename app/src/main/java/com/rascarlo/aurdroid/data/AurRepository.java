/*
 *     Copyright (C) rascarlo  rascarlo@gmail.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rascarlo.aurdroid.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.rascarlo.aurdroid.BuildConfig;
import com.rascarlo.aurdroid.api.AurService;
import com.rascarlo.aurdroid.api.model.Info;
import com.rascarlo.aurdroid.api.model.Search;
import com.rascarlo.aurdroid.util.AurdroidConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AurRepository {

    private final AurService aurService;
    private static AurRepository aurRepository;

    private AurRepository() {
        // http logging interceptor
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        // okhttp client
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.readTimeout(30, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(httpLoggingInterceptor);
        // retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AurdroidConstants.AUR_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        // service
        aurService = retrofit.create(AurService.class);
    }

    public synchronized static AurRepository getArchPackagesRepositoryInstance() {
        if (aurRepository == null) {
            aurRepository = new AurRepository();
        }
        return aurRepository;
    }

    AurService getAurService() {
        return aurService;
    }

    public LiveData<Search> getAurSearchLiveData(final int field, final String query) {
        final MutableLiveData<Search> aurSearchMutableLiveData = new MutableLiveData<>();
        Call<Search> aurSearchCall;
        switch (field) {
            case AurdroidConstants.SEARCH_PARAMETER_NAME_OR_DESCRIPTION:
                aurSearchCall = aurService.searchByNameOrDescription(query);
                break;
            case AurdroidConstants.SEARCH_PARAMETER_NAME:
                aurSearchCall = aurService.searchByName(query);
                break;
            case AurdroidConstants.SEARCH_PARAMETER_MAINTAINER:
                aurSearchCall = aurService.searchByMaintainer(query);
                break;
            case AurdroidConstants.SEARCH_PARAMETER_DEPENDS:
                aurSearchCall = aurService.searchByDepends(query);
                break;
            case AurdroidConstants.SEARCH_PARAMETER_MAKE_DEPENDS:
                aurSearchCall = aurService.searchByMakeDepends(query);
                break;
            case AurdroidConstants.SEARCH_PARAMETER_OPT_DEPENDS:
                aurSearchCall = aurService.searchByOptDepends(query);
                break;
            case AurdroidConstants.SEARCH_PARAMETER_CHECK_DEPENDS:
                aurSearchCall = aurService.searchByCheckDepends(query);
                break;
            default:
                aurSearchCall = aurService.searchByNameOrDescription(query);
                break;
        }
        aurSearchCall.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(@NonNull Call<Search> call, @NonNull Response<Search> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == 200) {
                    aurSearchMutableLiveData.setValue(response.body());
                } else {
                    aurSearchMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Search> call, @NonNull Throwable t) {
                aurSearchMutableLiveData.setValue(null);
            }
        });
        return aurSearchMutableLiveData;
    }

    public LiveData<Info> getInfoLiveData(final String pkgname) {
        final MutableLiveData<Info> infoMutableLiveData = new MutableLiveData<>();
        Call<Info> infoCall = aurService.searchInfo(pkgname);
        infoCall.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(@NonNull Call<Info> call, @NonNull Response<Info> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == 200) {
                    infoMutableLiveData.setValue(response.body());
                } else {
                    infoMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Info> call, @NonNull Throwable t) {
                infoMutableLiveData.setValue(null);
            }
        });
        return infoMutableLiveData;
    }
}
