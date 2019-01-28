package com.khoerulabu.pulsaapp.service

import com.khoerulabu.pulsaapp.model.Users
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersService {
    @GET("login")
    fun login(@Query("username") username : String,
              @Query("password") password : String) : Deferred<Response<Users>>
}