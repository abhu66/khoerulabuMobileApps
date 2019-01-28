package com.khoerulabu.pulsaapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction (
    @SerializedName("id")            var  id           : Long?     = null,
    @SerializedName("phoneNumber")   var  phoneNumber  : String?   = null,
    @SerializedName("users")         var  users        : Users?    = null,
    @SerializedName("operator")      var  operator     : Operator? = null,
    @SerializedName("voucher")       var  voucher      : Voucher?  = null,
    @SerializedName("createdAt")     var  createdDate  : String?   = null
) :Parcelable