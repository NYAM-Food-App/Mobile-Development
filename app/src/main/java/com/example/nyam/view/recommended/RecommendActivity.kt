package com.example.nyam.view.recommended

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nyam.BaseClass
import com.example.nyam.R
import com.example.nyam.adapter.RecipesAdapter
import com.example.nyam.data.ResultState
import com.example.nyam.databinding.ActivityRecommendBinding
import com.example.nyam.helper.ViewModelFactory

class RecommendActivity : AppCompatActivity() {
    private val viewModel by viewModels<RecommendedViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var _binding : ActivityRecommendBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvFood.layoutManager = LinearLayoutManager(this)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getRecipes().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        // Handle loading state
                    }
                    is ResultState.Success -> {
                        val adapter =RecipesAdapter()
                        adapter.submitList(result.data)
                        binding.rvFood.adapter = adapter
                        // Update UI with recipes
                    }
                    is ResultState.Error -> {
                        val errorMessage = result.error
                        // Handle error state
                    }
                }
            }
        }
    }
}