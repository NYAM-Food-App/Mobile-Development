package com.example.nyam.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nyam.data.remote.response.FulfilledNeeds

@Entity(tableName = "recipes")
class RecipesEntity(
        @PrimaryKey(autoGenerate = true)
        val index:Int = 0,

        val image: String,

        val foodname: String,

        val dishType: String,

        val mealType: String,

        val howToCook: String,

        val ingredients: String,

        val sourceRecipes: String,

        val cuisineType: String,

        val calories: String,
        val fat : String,
        val carbs : String,
        val protein : String,
)