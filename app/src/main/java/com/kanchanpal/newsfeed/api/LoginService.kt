package com.kanchanpal.newsfeed.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "X-API-KEY: 454041184B0240FBA3AACD15A1F7A8BB"
    )
    @FormUrlEncoded
    @POST("api/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): User

    companion object {
        const val ENDPOINT = "https://talentpool.oneindonesia.id/"
    }
}