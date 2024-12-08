package com.example.nyam.data.remote.response

import com.google.gson.annotations.SerializedName

data class AnalyzeResponse(

	@field:SerializedName("requestTime")
	val requestTime: String,

	@field:SerializedName("recipes")
	val recipes: List<RecipesItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("foodPrediction")
	val foodPrediction: FoodPrediction
)

data class FoodPrediction(

	@field:SerializedName("predictedClass")
	val predictedClass: String,

	@field:SerializedName("predictedProb")
	val predictedProb: Any
)


data class RecipesItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("foodname")
	val foodname: String,

	@field:SerializedName("dishType")
	val dishType: List<String>?= null,

	@field:SerializedName("mealType")
	val mealType: List<String>,

	@field:SerializedName("how to cook")
	val howToCook: String,

	@field:SerializedName("ingredients")
	val ingredients: List<String>,

	@field:SerializedName("source recipes")
	val sourceRecipes: String,

	@field:SerializedName("cuisineType")
	val cuisineType: List<String>,

	@field:SerializedName("fulfilledNeeds")
	val fulfilledNeeds: FulfilledNeeds
)
