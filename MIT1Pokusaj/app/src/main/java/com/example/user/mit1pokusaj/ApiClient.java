package com.example.user.mit1pokusaj;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "https://makeup-api.herokuapp.com/api/v1/";
    public static Retrofit retrofit;

    public static Retrofit getApiClient(){
        if(retrofit==null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();
        }

        return retrofit;
    }


}
