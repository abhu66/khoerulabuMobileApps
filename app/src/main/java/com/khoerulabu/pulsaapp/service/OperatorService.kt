package com.khoerulabu.pulsaapp.service

import com.khoerulabu.pulsaapp.model.Operator
import com.khoerulabu.pulsaapp.model.Users
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OperatorService {
    @GET("operator")
    fun getAllOperator() : Deferred<Response<List<Operator>>>
}