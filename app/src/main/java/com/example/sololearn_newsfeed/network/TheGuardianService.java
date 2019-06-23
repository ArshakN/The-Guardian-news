package com.example.sololearn_newsfeed.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheGuardianService {

//    @GET("search")
//    Call<Response> getNews(@Query("api-key") String apiKey);

    @GET("search")
    Call<News> getNews(
            @Query("api-key") String apiKey,
            @Query("show-fields") String showThumbs,
            @Query("page") Integer currentPage);

}
