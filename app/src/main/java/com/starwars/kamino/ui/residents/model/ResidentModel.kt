package com.starwars.kamino.ui.residents.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResidentModel(
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: String,
    @SerializedName("mass") val mass: String,
    @SerializedName("hair_color") val hairColor: String,
    @SerializedName("skin_color") val skinColor: String,
    @SerializedName("eye_color") val eyeColor: String,
    @SerializedName("birth_year") val birthYear: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("homeworld") val homeWorld: String,
    @SerializedName("created") val created: String,
    @SerializedName("edited") val edited: String,
    @SerializedName("image_url") val imageUrl: String
): Parcelable