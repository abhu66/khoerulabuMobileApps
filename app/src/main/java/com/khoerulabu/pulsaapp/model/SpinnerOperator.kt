package com.khoerulabu.pulsaapp.model

data class SpinnerOperator (
    val operatorName : String?,
    val id : Long?

){
    override fun toString() = operatorName!!
}