package com.example.nyam.data.remote.retrofit

import com.example.nyam.data.remote.response.AnalyzeResponse
import com.example.nyam.data.remote.response.HistoryResponse
import com.example.nyam.data.remote.response.ChosenFood
import com.example.nyam.data.remote.response.PostResponse
import com.example.nyam.data.remote.response.RegisterBody
import com.example.nyam.data.remote.response.TextBody
import com.example.nyam.data.remote.response.UpdateBody
import com.example.nyam.data.remote.response.UserData
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {


    @GET("user/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): UserData


    @Multipart
    @POST("analyze/{id}")
    suspend fun analyzeImage(
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): AnalyzeResponse


    @POST("analyze/text/{id}")
    suspend fun analyzeFood(
        @Path("id") id: String,
        @Body queryText :TextBody
    ): AnalyzeResponse


    @POST("choose/food/{id}")
    suspend fun chooseFood(
        @Path("id") id: String,
        @Body selectedIndex: ChosenFood
    ): PostResponse

    @GET("history/food/{id}")
    suspend fun getHistory(
        @Path("id") id: String
    ): HistoryResponse

    @POST("auth/register")
    suspend fun registerUser(
        @Body userData: RegisterBody
    ): PostResponse

    @PUT("user/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body userData: UpdateBody
    ):PostResponse
}