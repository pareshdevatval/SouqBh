package com.souqbh.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseResponse : Parcelable {

    @SerializedName("message")
    val message: String = ""
    @SerializedName("status")
    val status: Boolean = false
    @SerializedName("code")
    val code: Int = 0
}