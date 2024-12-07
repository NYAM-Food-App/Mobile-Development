package com.example.nyam.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nyam.data.local.entity.RecipesEntity

@Dao
interface RecipesDao {

    @Query("SELECT * FROM recipes ORDER BY `index` DESC")
    fun getRecipes(): LiveData<List<RecipesEntity>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipes(recipes: List<RecipesEntity>)


    @Query("DELETE FROM recipes ")
    suspend fun deleteAllRecipes()
}