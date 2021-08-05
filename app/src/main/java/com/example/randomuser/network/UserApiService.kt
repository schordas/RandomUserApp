package com.example.randomuser.network

import com.example.randomuser.model.User
import com.example.randomuser.model.Wrapped
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {

    @Wrapped
    @GET(".")
    suspend fun getUsers(@Query("results") results: Int): Response<List<User>>

}
