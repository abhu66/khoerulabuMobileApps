package com.khoerulabu.pulsaapp.service

import com.khoerulabu.pulsaapp.model.Pulsa
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PulsaService {
    @GET("pulsa/pulsabyoperator")
    fun getListPulsaByOpId(@Query("operatorId") operatorId : Long) : Deferred<Response<List<Pulsa>>>
}