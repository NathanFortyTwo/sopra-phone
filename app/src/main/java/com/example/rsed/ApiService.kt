package com.example.rsed

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/")
    fun sendData(@Query("data") data: String): Call<String>
}