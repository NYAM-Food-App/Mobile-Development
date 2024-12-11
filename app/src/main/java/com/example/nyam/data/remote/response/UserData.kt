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
	val dailyNeeds: DailyNeeds,

	@field:SerializedName("profilePicture")
	val profilePicture: Any? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("bmrRate")
	val bmrRate: Int,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("fulfilledNeeds")
	val fulfilledNeeds: FulfilledNeeds,

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
	val imageUrl: String,

	@field:SerializedName("selectedFood")
	val selectedFood: SelectedFood,

	@field:SerializedName("timestamp")
	val timestamp: Timestamp? = null
)

data class FulfilledNeeds(

	@field:SerializedName("carbs")
	val carbs: Any,

	@field:SerializedName("protein")
	val protein: Any,

	@field:SerializedName("fat")
	val fat: Any,

	@field:SerializedName("calories")
	val calories: Any
)

data class SelectedFood(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("foodname")
	val foodname: String,

	@field:SerializedName("dishType")
	val dishType: List<String?>? = null,

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

data class DailyNeeds(

	@field:SerializedName("carbs")
	val carbs: Int,

	@field:SerializedName("protein")
	val protein: Int,

	@field:SerializedName("fat")
	val fat: Int,

	@field:SerializedName("calories")
	val calories: Int
)

data class Timestamp(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)

data class RegisterBody(
	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("fullname")
	val fullname: String,
	@field:SerializedName("birthdate")
	val birthdate: String,

	@field:SerializedName("gender")
	val gender: Int,

	@field:SerializedName("allergy")
	val allergy: List<String>,

	@field:SerializedName("height")
	val height: Int,

	@field:SerializedName("weight")
	val weight: Int

)

data class UpdateBody(

	@field:SerializedName("fullname")
	val fullname: String,
	@field:SerializedName("birthdate")
	val birthdate: String,

	@field:SerializedName("gender")
	val gender: Int,

	@field:SerializedName("allergy")
	val allergy: List<String>,

	@field:SerializedName("height")
	val height: Int,

	@field:SerializedName("weight")
	val weight: Int

)
data class TextBody(

	@field:SerializedName("queryText")
	val queryText: String,
)

