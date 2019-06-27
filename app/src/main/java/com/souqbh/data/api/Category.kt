package com.souqbh.data.api

import com.google.gson.annotations.SerializedName

data class Category(

    @SerializedName("c_id") val c_id: Int,
    @SerializedName("c_name") val c_name: String,
    @SerializedName("c_image") val c_image: String,
    @SerializedName("sub_categories") val sub_categories: List<SubCategory>
)