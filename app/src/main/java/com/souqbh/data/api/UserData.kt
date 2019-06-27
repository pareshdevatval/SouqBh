package com.souqbh.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    @SerializedName("u_id") val u_id: Int,
    @SerializedName("u_first_name") val u_first_name: String?,
    @SerializedName("u_last_name") val u_last_name: String?,
    @SerializedName("u_user_name") val u_user_name: String?,
    @SerializedName("u_email") val u_email: String?,
    @SerializedName("u_mobile_number") val u_mobile_number: String?,
    @SerializedName("u_image") val u_image: String?,
    @SerializedName("u_otp") val u_otp: String?,
    @SerializedName("u_social_id") val u_social_id: String?,
    @SerializedName("u_is_verified") val u_is_verified: Int,
    @SerializedName("u_user_type") val u_user_type: Int,
    @SerializedName("u_last_login") val u_last_login: String?,
    @SerializedName("u_status") val u_status: Int,
    @SerializedName("u_created_at") val u_created_at: String?,
    @SerializedName("u_updated_at") val u_updated_at: String?,
    @SerializedName("u_deleted_at") val u_deleted_at: String?,
    @SerializedName("token") val token: String?
) : Parcelable