package com.kanchanpal.newsfeed.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("data") val data: UserData,
    val message: String,
    val status: Boolean,
    val token: String
): Parcelable

@Parcelize
data class UserData(
    val avatar: String,
    val banned: String,
    val company_id: String,
    val date_created: String,
    val email: String,
    val forgot_exp: String,
    val full_name: String,
    val id: String,
    val ip_address: String,
    val last_activity: String,
    val last_login: String,
    val oauth_provider: String,
    val oauth_uid: String,
    val remember_exp: String,
    val remember_time: String,
    val top_secret: String,
    val username: String,
    val verification_code: String
): Parcelable