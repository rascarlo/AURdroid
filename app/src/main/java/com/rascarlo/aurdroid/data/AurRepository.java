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

import com.rascarlo.aurdroid.BuildConfig;
import com.rascarlo.aurdroid.api.AurService;
import com.rascarlo.aurdroid.util.AurdroidConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AurRepository {

    private final AurService aurService;
    private static AurRepository aurRepository;

    private AurRepository() {
        // http logging interceptor
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
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

    public synchronized static AurRepository getAurRepositoryInstance() {
        if (aurRepository == null) {
            aurRepository = new AurRepository();
        }
        return aurRepository;
    }

    public AurService getAurService() {
        return aurService;
    }
}
