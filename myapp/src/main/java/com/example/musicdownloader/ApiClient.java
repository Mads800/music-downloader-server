package com.example.musicdownloader;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // استبدل هذا العنوان بعنوان سيرفرك الجديد
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)   // هنا يستعمل العنوان الجديد
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
