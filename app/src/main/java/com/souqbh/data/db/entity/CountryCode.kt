package com.souqbh.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "CountryCode")
data class CountryCode(
    @SerializedName("name") @ColumnInfo val name: String,
    @SerializedName("dial_code") @ColumnInfo val dial_code: String,
    @PrimaryKey @SerializedName("code") @ColumnInfo val code: String
) : Parcelable