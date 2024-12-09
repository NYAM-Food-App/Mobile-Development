package com.example.nyam.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
class HistoryEntity(

    val image: String,

    @PrimaryKey
    val id : Int,

    val foodname: String,

    val dishType: String? = null,

    val mealType: String,

    val howToCook: String,

    val ingredients: String,

    val sourceRecipes: String,

    val cuisineType: String,

    val calories: Double,
    val fat: Double,
    val carbs: Double,
    val protein: Double,
)