package com.khoerulabu.pulsaapp.service

import com.khoerulabu.pulsaapp.model.Transaction
import kotlinx.coroutines.Deferred
import retrofit2.http.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST



interface TransactionService {
    @GET("transaction/listbyuserid")
    fun getListTransactionByUserId(@Query("usersId") usersId : Long?) : Deferred<Response<List<Transaction>>>


    @POST("transaction")
    fun saveTransaction(@Body body : HashMap<String, Any?>): Deferred<Response<ResponseBody>>
}