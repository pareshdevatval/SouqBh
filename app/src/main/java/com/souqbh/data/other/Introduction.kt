package com.souqbh.data.other

import android.os.Parcel
import android.os.Parcelable

data class Introduction(
    val strTitle: String,
    val strMessage: String,
    val imageId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel!!.writeString(strTitle)
        parcel.writeString(strMessage)
        parcel.writeInt(imageId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Introduction> {
        override fun createFromParcel(parcel: Parcel): Introduction {
            return Introduction(parcel)
        }

        override fun newArray(size: Int): Array<Introduction?> {
            return arrayOfNulls(size)
        }
    }
}