package com.khoerulabu.pulsaapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Pulsa (
    @SerializedName("id")            var  id           : Long?     = null,
    @SerializedName("pulsa")         var  pulsa        : String?   = null,
    @SerializedName("operator")      var  operator     : Operator? = null,
    @SerializedName("createdAt")     var  createdDate  : Date?     = null,
    @SerializedName("updatedAt")     var  updatedDate  : Date?     = null
) :Parcelable