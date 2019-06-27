package com.souqbh.data.api

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

    @SerializedName("result") val result: List<Category>,
    @SerializedName("banners") val banners: List<String>,
    @SerializedName("per_page") val per_page: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("current_page") val current_page: Int,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Boolean
)