package com.example.nyam.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nyam.data.local.entity.RecipesEntity

@Dao
interface RecipesDao {

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getRecipes(): LiveData<List<RecipesEntity>>

    @Query("SELECT * FROM recipes WHERE id =:id ORDER  BY `id` DESC")
    fun getDetailRecipes(id: Int): LiveData<RecipesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipesEntity>)


    @Query("DELETE FROM recipes ")
    suspend fun deleteAllRecipes()
}