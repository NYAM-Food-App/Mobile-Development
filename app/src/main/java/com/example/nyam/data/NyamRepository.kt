package com.example.nyam.data

import android.util.Log
import androidx.lifecycle.liveData
import com.example.nyam.data.pref.UserModel
import com.example.nyam.data.pref.UserPreference
import com.example.nyam.data.remote.response.AnalyzeResponse
import com.example.nyam.data.remote.response.UserData
import com.example.nyam.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class NyamRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun getUser(): UserData {
        return apiService.getUser()
    }

    fun uploadImage(imageFile:File)= liveData{
        emit(ResultState.Loading)

        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(multipartBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.d("REPOSITORY WOIIIIIIIIII", "uploadImage: $errorBody")
            val errorResponse = Gson().fromJson(errorBody, AnalyzeResponse::class.java)
            emit(ResultState.Error(errorBody.toString()))
        }catch (e:Exception){
            emit(ResultState.Error(e.message.toString()))
        }

    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: NyamRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): NyamRepository = instance ?: synchronized(this) {
            instance ?: NyamRepository(userPreference,apiService)
        }.also { instance = it }
    }
}