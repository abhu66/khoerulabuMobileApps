package com.khoerulabu.pulsaapp.presenter

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.khoerulabu.pulsaapp.PulsaDBApi
import com.khoerulabu.pulsaapp.service.*
import com.khoerulabu.pulsaapp.view.BaseView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Presenter (
    private val baseView: BaseView
){
    private fun retrofitBuilder() : Retrofit{
        baseView.showLoading()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(PulsaDBApi.baseUrl())
            .build()
        baseView.hideLoading()
        return retrofit
    }

    fun createOpearatorPresenter(): OperatorService {
        val retrofit = retrofitBuilder()
        return retrofit.create(OperatorService::class.java)
    }

    fun createPulsaPresenter(): PulsaService {
        val retrofit = retrofitBuilder()
        return retrofit.create(PulsaService::class.java)
    }

    fun createUsersPresenter(): UsersService {
        val retrofit = retrofitBuilder()
        return retrofit.create(UsersService::class.java)
    }

    fun createVoucherPresenter(): VoucherService {
        val retrofit = retrofitBuilder()
        return retrofit.create(VoucherService::class.java)
    }
    fun createTransactionPresenter(): TransactionService {
        val retrofit = retrofitBuilder()
        return retrofit.create(TransactionService::class.java)
    }
}