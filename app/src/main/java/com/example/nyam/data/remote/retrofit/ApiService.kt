package com.example.nyam.data.remote.retrofit

import com.example.nyam.data.remote.response.UserData
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    //TODO: NEED TO DELETE STATIC STRING
    @GET("user/{id}")
    suspend fun getUser(
        @Path("id") id:String = "W82AJqbULgU2nQg1mRUXrfFXVEu1"
    ):UserData
}