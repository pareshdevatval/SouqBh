package com.souqbh.data.db

import com.google.gson.annotations.SerializedName
import com.souqbh.data.db.entity.CountryCode

data class CountryCodeResponse(
    @SerializedName("data") val data: List<CountryCode>
)