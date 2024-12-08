package com.example.nyam.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.nyam.data.local.dao.RecipesDao
import com.example.nyam.data.local.entity.RecipesEntity
import com.example.nyam.data.pref.UserModel
import com.example.nyam.data.pref.UserPreference
import com.example.nyam.data.remote.response.AnalyzeResponse
import com.example.nyam.data.remote.response.FoodHistoryItem
import com.example.nyam.data.remote.response.PostBody
import com.example.nyam.data.remote.response.PostResponse
import com.example.nyam.data.remote.response.RecipesItem
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
    private val apiService: ApiService,
    private val recipesDao: RecipesDao

) {

    suspend fun getUser(): UserData {
        return apiService.getUser()
    }

    fun chooseFood(id: String, index: Int) : LiveData<ResultState<PostResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.chooseFood(id, PostBody(index))
            emit(ResultState.Success(response))
        }catch ( e: HttpException){
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun getDetailRecipes(id: Int) = recipesDao.getDetailRecipes(id)

    fun getRecipes(): LiveData<ResultState<List<RecipesEntity>>> = liveData {
        emit(ResultState.Loading)
        try {
            val localData: LiveData<ResultState<List<RecipesEntity>>> =
                recipesDao.getRecipes().map { ResultState.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

fun uploadImage(imageFile: File) = liveData {
    emit(ResultState.Loading)
    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
    val multipartBody = MultipartBody.Part.createFormData(
        "file",
        imageFile.name,
        requestImageFile
    )
    try {
        val successResponse = apiService.uploadImage(multipartBody)
        var id = 0
        val recipesList = successResponse.recipes.map { recipe ->

            RecipesEntity(
                id = id++,
                image = recipe.image,
                foodname = recipe.foodname,
                dishType = recipe.dishType.toString(),
                mealType = recipe.mealType.toString(),
                howToCook = recipe.howToCook,
                ingredients = recipe.ingredients.toString(),
                sourceRecipes = recipe.sourceRecipes,
                cuisineType = recipe.cuisineType.toString(),
                calories = recipe.fulfilledNeeds.calories as Double,
                fat = recipe.fulfilledNeeds.fat as Double,
                carbs = recipe.fulfilledNeeds.carbs as Double,
                protein = recipe.fulfilledNeeds.protein as Double
            )
        }
        recipesDao.deleteAllRecipes()
        recipesDao.insertRecipes(recipesList)
        emit(ResultState.Success(successResponse))
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        Log.d("REPOSITORY WOIIIIIIIIII", "uploadImage: $errorBody")
//        val errorResponse = Gson().fromJson(errorBody, AnalyzeResponse::class.java)
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
        apiService: ApiService,
        recipesDao: RecipesDao
    ): NyamRepository = instance ?: synchronized(this) {
        instance ?: NyamRepository(userPreference, apiService, recipesDao)
    }.also { instance = it }
}
}