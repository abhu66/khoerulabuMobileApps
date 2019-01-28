package com.khoerulabu.pulsaapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Users (
    @SerializedName("id")                   var id                : Long?   = null,
    @SerializedName("username")             var  username         : String? = null,
    @SerializedName("password")             var  password         : String? = null,
    @SerializedName("createdAt")            var  createdDate      : Date?   = null,
    @SerializedName("updatedAt")            var  updatedDate      : Date?   = null
) : Parcelable
