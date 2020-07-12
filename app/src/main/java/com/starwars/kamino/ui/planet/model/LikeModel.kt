package com.starwars.kamino.ui.planet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LikeModel(
    @SerializedName("planet_id") val planetId: Int,
    @SerializedName("likes ") val likes: Int
) : Parcelable