package com.example.nyam.view.detail

import androidx.lifecycle.ViewModel
import com.example.nyam.data.NyamRepository

class FoodDetailViewModel(private val repository: NyamRepository) : ViewModel()  {

    fun getDetailRecipes(id : Int) = repository.getDetailRecipes(id)

    fun chooseFood(id: String, index: Int) = repository.chooseFood(id,index)
}