package com.fullsekurity.urbandict.repository.storage

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(

    @SerializedName(value = "country") var country: String,
    @SerializedName(value = "name") var name: String,
    @SerializedName(value = "_id") var id: Int,
    @SerializedName(value = "coord") var coord: Coordinates

) : Parcelable

@Parcelize
data class Coordinates(

    @SerializedName(value = "lon") var lon: Float,
    @SerializedName(value = "lat") var lat: Float

) : Parcelable