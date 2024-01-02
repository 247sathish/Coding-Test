package com.app.wipro.network

import com.app.wipro.model.LoginRequest
import com.app.wipro.model.LoginResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface WChangeAPI {

    @POST("auth/login")
    @Headers("Accept: application/json")
    fun postData(@Body user: JsonObject): Call<LoginResponse>
}