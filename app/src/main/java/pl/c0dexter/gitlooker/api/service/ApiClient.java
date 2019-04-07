package pl.c0dexter.gitlooker.api.service;


import android.util.Log;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pl.c0dexter.gitlooker.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String TAG = ApiClient.class.getClass().getSimpleName();
    private static final String BASE_API_URL = "https://api.github.com/search/";
    private static final String API_KEY = BuildConfig.ApiKey;

    public static Retrofit getClient() {
        OkHttpClient.Builder client = setHttpClientBuilder();
        return new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    private static OkHttpClient.Builder setHttpClientBuilder() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor((Interceptor.Chain chain) -> {
            Request original = chain.request();
            HttpUrl httpUrl = HttpUrl.parse(String.valueOf(original.url()));

            Request.Builder requestBuilder = original.newBuilder().url(httpUrl);
            Request request = requestBuilder
                    .addHeader("Accept", "application/vnd.github.preview.text-match+json")
                    .addHeader("Token", API_KEY)
                    .build();

            Log.d(TAG, "Request URL:" + requestBuilder.toString());

            return chain.proceed(request);
        });
        return httpClient;
    }
}
