package com.example.nyam.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserData(

	@field:SerializedName("birthdate")
	val birthdate: String? = null,

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("foodHistory")
	val foodHistory: List<FoodHistoryItem?>? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("allergy")
	val allergy: List<String?>? = null,

	@field:SerializedName("dailyNeeds")
	val dailyNeeds: DailyNeeds? = null,

	@field:SerializedName("profilePicture")
	val profilePicture: Any? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("bmrRate")
	val bmrRate: Int? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("fulfilledNeeds")
	val fulfilledNeeds: FulfilledNeeds? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("bmi")
	val bmi: Any? = null,

	@field:SerializedName("height")
	val height: Int? = null,

	@field:SerializedName("selectedFood")
	val selectedFood: SelectedFood? = null
)

data class FoodHistoryItem(

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("selectedFood")
	val selectedFood: SelectedFood? = null,

	@field:SerializedName("timestamp")
	val timestamp: Timestamp? = null
)

data class FulfilledNeeds(

	@field:SerializedName("carbs")
	val carbs: Any? = null,

	@field:SerializedName("protein")
	val protein: Any? = null,

	@field:SerializedName("fat")
	val fat: Any? = null,

	@field:SerializedName("calories")
	val calories: Any? = null
)

data class SelectedFood(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("foodname")
	val foodname: String? = null,

	@field:SerializedName("dishType")
	val dishType: List<String?>? = null,

	@field:SerializedName("mealType")
	val mealType: List<String?>? = null,

	@field:SerializedName("how to cook")
	val howToCook: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: List<String?>? = null,

	@field:SerializedName("source recipes")
	val sourceRecipes: String? = null,

	@field:SerializedName("cuisineType")
	val cuisineType: List<String?>? = null,

	@field:SerializedName("fulfilledNeeds")
	val fulfilledNeeds: FulfilledNeeds? = null
)

data class DailyNeeds(

	@field:SerializedName("carbs")
	val carbs: Int? = null,

	@field:SerializedName("protein")
	val protein: Int? = null,

	@field:SerializedName("fat")
	val fat: Int? = null,

	@field:SerializedName("calories")
	val calories: Int? = null
)

data class Timestamp(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)
