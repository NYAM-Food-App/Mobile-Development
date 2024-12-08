package com.example.nyam.view.detail

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nyam.MainActivity
import com.example.nyam.R
import com.example.nyam.data.ResultState
import com.example.nyam.databinding.ActivityFoodDetailBinding
import com.example.nyam.helper.ViewModelFactory

class FoodDetailActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityFoodDetailBinding
    private val binding get() = _binding
    private val viewModel by viewModels<FoodDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setActionBar()
        val id= intent.getIntExtra(FOOD_ID,0)
        val loadingDialog = AlertDialog.Builder(this).setView(R.layout.dialog_builder).create()

        binding.btnEat.setOnClickListener {
            viewModel.chooseFood("W82AJqbULgU2nQg1mRUXrfFXVEu1",id).observe(this){ result->
                when(result){
                    is ResultState.Loading -> {
                        loadingDialog.show()
                    }
                    is ResultState.Success -> {
                        loadingDialog.dismiss()
                        Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this    ,MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    is ResultState.Error -> {
                        loadingDialog.dismiss()
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        observeViewModel(id)

    }

    private fun observeViewModel(id: Int) {
        viewModel.getDetailRecipes(id).observe(this){ recipe ->
            with(binding){
                val myFormat = DecimalFormat("#")

                tvFood.text = recipe.foodname
                Glide.with(this@FoodDetailActivity).load(recipe.image).apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                ).into(ivFoodPhoto)
                tvFat.text = myFormat.format(recipe.fat)
                tvCarbs.text = myFormat.format(recipe.carbs)
                tvProtein.text = myFormat.format(recipe.protein)
                valueIngredients.text = recipe.ingredients.substring(1, recipe.ingredients.length - 1).replace(", ","\n")
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