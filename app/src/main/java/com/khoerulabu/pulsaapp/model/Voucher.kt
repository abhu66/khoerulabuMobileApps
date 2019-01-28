package com.khoerulabu.pulsaapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Voucher (
    @SerializedName("id")            var  id           : Long?     = null,
    @SerializedName("hargaVoucher")  var  hargaVoucher : Double?   = null,
    @SerializedName("operator")      var  operatorId   : Operator? = null,
    @SerializedName("pulsa")         var  pulsaId      : Pulsa?    = null,
    @SerializedName("createdAt")     var  createdDate  : Date?     = null,
    @SerializedName("updatedAt")     var  updatedDate  : Date?     = null
) : Parcelable