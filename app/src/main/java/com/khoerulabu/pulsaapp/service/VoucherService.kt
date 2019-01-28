package com.khoerulabu.pulsaapp.service
import com.khoerulabu.pulsaapp.model.Voucher
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface VoucherService {
    @GET("voucher/hargavoucher")
    fun findByOperatorIdAndPulsaId(@Query("operatorId") operatorId : Long?,
                                   @Query("pulsaId") pulsaId : Long?) : Deferred<Response<Voucher>>


}