package com.souqbh.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDataResponse(@SerializedName("result") val userData: UserData) : BaseResponse(), Parcelable