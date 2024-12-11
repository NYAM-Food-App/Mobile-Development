package com.example.nyam.view.detail

import android.content.Intent
import android.icu.text.DecimalFormat
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nyam.R
import com.example.nyam.databinding.ActivityFoodDetailHistoryBinding
import com.example.nyam.helper.ViewModelFactory

class FoodDetailHistoryActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityFoodDetailHistoryBinding
    private val binding get() = _binding
    private val viewModel by viewModels<FoodDetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityFoodDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val id = intent.getIntExtra(FOOD_ID, 0)
        setActionBar()
        observeViewModel(id)

        binding.tvLink.setOnClickListener {
            val intentUrl = Intent(Intent.ACTION_VIEW, Uri.parse(binding.tvLink.text.toString()))
            binding.root.context.startActivity(intentUrl)
        }
    }

    private fun observeViewModel(id: Int) {
        viewModel.getDetailHistory(id).observe(this) { recipe ->
            with(binding) {
                val myFormat = DecimalFormat("#")

                tvFood.text = recipe.foodname
                Glide.with(this@FoodDetailHistoryActivity).load(recipe.image).apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                ).into(ivFoodPhoto)
                tvFat.text = myFormat.format(recipe.fat)
                tvCarbs.text = myFormat.format(recipe.carbs)
                tvProtein.text = myFormat.format(recipe.protein)
                valueIngredients.text =
                    recipe.ingredients.substring(1, recipe.ingredients.length - 1)
                        .replace(", ", "\n")
                tvLink.text = recipe.howToCook
            }
        }
    }

    private fun setActionBar() {
        supportActionBar?.setCustomView(R.layout.app_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    companion object {
        const val FOOD_ID = "food_id"
    }
}