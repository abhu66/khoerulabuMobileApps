package com.khoerulabu.pulsaapp.model

data class SpinnerPulsa (
    val pulsa : String?,
    val id : Long?
){
    override fun toString() = pulsa!!
}