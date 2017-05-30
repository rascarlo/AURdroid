package com.rascarlo.aurdroid.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AurRpcService {

    private static final String LOG_TAG = AurRpcService.class.getSimpleName();
    private static final String AUR_RPC_BASE_URL = "https://aur.archlinux.org/rpc/";

    /**
     * https://wiki.archlinux.org/index.php/AurJson
     * https://aur.archlinux.org/
     */
    public static synchronized AurRpcApi getAurRpcApi() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {

                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        HttpUrl httpUrl = chain.request().url()
                                .newBuilder()
                                .build();
                        Request request = chain.request().newBuilder().url(httpUrl).build();
                        Log.d(LOG_TAG, "Request URL: " + request.url().toString());
                        return chain.proceed(request);
                    }
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AUR_RPC_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(String.class, new NullStringTypeAdapter())
                        .serializeNulls()
                        .create()))
                .client(okHttpClient)
                .build();
        return retrofit.create(AurRpcApi.class);
    }

    /**
     * TypeAdapter to replace null string with ""
     * Mostly used to replace empty maintainer string
     *
     * @see com.google.gson.TypeAdapter
     * {@link TypeAdapter}
     * https://google.github.io/gson/apidocs/com/google/gson/TypeAdapter.html
     */
    private static class NullStringTypeAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter out, String value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return "";
            }
            return in.nextString();
        }
    }
}
