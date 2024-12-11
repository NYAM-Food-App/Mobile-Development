package com.example.nyam.view.recommended

import androidx.lifecycle.ViewModel
import com.example.nyam.data.NyamRepository

class RecommendedViewModel(private val repository: NyamRepository) : ViewModel() {

    fun getRecipes() = repository.getRecipes()

    fun analyzeFood(id: String, text: String) = repository.analyzeFood(id, text)
}