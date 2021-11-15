package com.kanchanpal.newsfeed.api

import retrofit2.Response
import retrofit2.http.*

interface NewsService {

    @GET("/v2/top-headlines")
    suspend fun getTopNewsList(

        @Query("apiKey") apiKey: String? = null, @Query("page") page : Int? = null,
        @Query("pageSize") pageSize : Int? = null,
        @Query("country") country: String? = null): Response<NewsListResponse>

    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "X-API-KEY: 454041184B0240FBA3AACD15A1F7A8BB"
    )
    @FormUrlEncoded
    @POST("/v2/top-headlines")
    suspend fun login(

        @Query("apiKey") apiKey: String? = null, @Query("page") page : Int? = null,
        @Query("pageSize") pageSize : Int? = null,
        @Query("country") country: String? = null): Response<NewsListResponse>

    companion object {
        const val ENDPOINT = "https://newsapi.org/"
    }

}
