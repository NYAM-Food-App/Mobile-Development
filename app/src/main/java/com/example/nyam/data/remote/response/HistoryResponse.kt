package com.example.nyam.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("foodHistory")
	val foodHistory: List<FoodHistoryItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)
data class PostResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("selectedFood")
	val selectedFood: SelectedFood? = null
)

data class ChosenFood(
	@field:SerializedName("selectedIndex")
	val selectedIndex : Int
)




