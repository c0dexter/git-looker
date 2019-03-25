package pl.c0dexter.gitlooker.api.service;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String TAG = ApiClient.class.getClass().getSimpleName();

    private static final String BASE_API_URL = "https://api.github.com/search/";

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
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .addHeader("Accept", "application/vnd.github.preview.text-match+json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Postman-Token", "329dcf1d-9dcb-4235-be34-82c855e28893")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        return httpClient;
    }
}
