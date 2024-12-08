package com.example.nyam.data.remote.retrofit

import com.example.nyam.data.remote.response.AnalyzeResponse
import com.example.nyam.data.remote.response.PostBody
import com.example.nyam.data.remote.response.PostResponse
import com.example.nyam.data.remote.response.UserData
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {


    //TODO: NEED TO DELETE STATIC STRING
    @GET("user/{id}")
    suspend fun getUser(
        @Path("id") id: String = "W82AJqbULgU2nQg1mRUXrfFXVEu1"
    ): UserData


    @Multipart
    @POST("analyze/{id}")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Path("id") id: String = "W82AJqbULgU2nQg1mRUXrfFXVEu1"
    ): AnalyzeResponse


    @POST("choose/food/{id}")
    suspend fun chooseFood(
        @Path("id") id: String = "W82AJqbULgU2nQg1mRUXrfFXVEu1",
        @Body selectedIndex: PostBody
    ): PostResponse

}