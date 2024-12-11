package com.example.nyam.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.nyam.data.local.dao.HistoryDao
import com.example.nyam.data.local.dao.RecipesDao
import com.example.nyam.data.local.entity.HistoryEntity
import com.example.nyam.data.local.entity.RecipesEntity
import com.example.nyam.data.remote.response.AnalyzeResponse
import com.example.nyam.data.remote.response.ChosenFood
import com.example.nyam.data.remote.response.HistoryResponse
import com.example.nyam.data.remote.response.PostResponse
import com.example.nyam.data.remote.response.RegisterBody
import com.example.nyam.data.remote.response.TextBody
import com.example.nyam.data.remote.response.UpdateBody
import com.example.nyam.data.remote.response.UserData
import com.example.nyam.data.remote.retrofit.ApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class NyamRepository private constructor(
    private val apiService: ApiService,
    private val recipesDao: RecipesDao,
    private val historyDao: HistoryDao
) {

    suspend fun registerUser(userData: RegisterBody): PostResponse {
        return apiService.registerUser(userData)
    }

    suspend fun updateUser(id:String,userData: UpdateBody): PostResponse {
        return apiService.updateUser(id,userData)
    }

    suspend fun getUser(id: String): UserData {
        return apiService.getUser(id)
    }

    fun chooseFood(id: String, index: Int): LiveData<ResultState<PostResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.chooseFood(id, ChosenFood(index))
            recipesDao.deleteAllRecipes()
            emit(ResultState.Success(response))
        }
        catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.d("REPOSITORY WOIIIIIIIIII", "chooseFood: $errorBody")
            val errorResponse = Gson().fromJson(errorBody, AnalyzeResponse::class.java)
            emit(ResultState.Error(errorResponse.toString()))
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

    fun analyzeImage(id: String, imageFile: File) = liveData {
        emit(ResultState.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.analyzeImage(id, multipartBody)
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
            emit(ResultState.Error(errorBody.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }
    fun analyzeFood(id: String, text: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.analyzeFood(id, TextBody(text))
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
            Log.d("REPOSITORY WOIIIIIIIIII", "uploadText: $errorBody")
//        val errorResponse = Gson().fromJson(errorBody, AnalyzeResponse::class.java)
            emit(ResultState.Error(errorBody.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun getHistory(id: String): LiveData<ResultState<List<HistoryEntity>>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getHistory(id)
            var id = 0
            val history = response.foodHistory.map { food ->
                HistoryEntity(
                    id = id++,
                    image = food.imageUrl,
                    foodname = food.selectedFood.foodname,
                    dishType = food.selectedFood.dishType.toString(),
                    mealType = food.selectedFood.mealType.toString(),
                    howToCook = food.selectedFood.howToCook,
                    ingredients = food.selectedFood.ingredients.toString(),
                    sourceRecipes = food.selectedFood.sourceRecipes,
                    cuisineType = food.selectedFood.cuisineType.toString(),
                    calories = food.selectedFood.fulfilledNeeds.calories as Double,
                    fat = food.selectedFood.fulfilledNeeds.fat as Double,
                    carbs = food.selectedFood.fulfilledNeeds.carbs as Double,
                    protein = food.selectedFood.fulfilledNeeds.protein as Double
                )
            }
            historyDao.deleteAllHistory()
            historyDao.insertHistory(history)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.d("REPOSITORY WOIIIIIIIIII", "GetHistory: $errorBody")
            val errorResponse = Gson().fromJson(errorBody, HistoryResponse::class.java)
            emit(ResultState.Error(errorBody.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
        val localData: LiveData<ResultState<List<HistoryEntity>>> =
            historyDao.getHistory().map { ResultState.Success(it) }
        emitSource(localData)
    }

    companion object {
        @Volatile
        private var instance: NyamRepository? = null
        fun getInstance(
            apiService: ApiService,
            recipesDao: RecipesDao,
            historyDao: HistoryDao
        ): NyamRepository = instance ?: synchronized(this) {
            instance ?: NyamRepository(apiService, recipesDao, historyDao)
        }.also { instance = it }
    }
}