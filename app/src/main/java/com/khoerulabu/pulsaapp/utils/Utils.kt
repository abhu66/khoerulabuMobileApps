package com.khoerulabu.pulsaapp.utils

import android.view.View
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun String.formatDateLocaleId(): String {

    val localeId = Locale("id")
    val formatter = SimpleDateFormat("yyyy-MM-dd", localeId)
    val sdf = SimpleDateFormat("EEE, d MMM yyyy", localeId)
    val date: Date = formatter.parse(this)

    return sdf.format(date)
}
