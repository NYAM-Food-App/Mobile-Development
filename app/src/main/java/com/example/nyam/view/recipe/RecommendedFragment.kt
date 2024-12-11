package com.example.nyam.view.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nyam.adapter.RecipesAdapter
import com.example.nyam.data.ResultState
import com.example.nyam.databinding.ActivityRecommendInputBinding
import com.example.nyam.helper.ViewModelFactory
import com.example.nyam.view.recommended.RecommendedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecommendedFragment: Fragment() {

    private var _binding: ActivityRecommendInputBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<RecommendedViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = ActivityRecommendInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        binding.rvFood.layoutManager = LinearLayoutManager(requireContext())
        binding.btnSearch.setOnClickListener {
            observeAnalyzeFood()
        }
        observeGetRecipes()
    }

    private fun observeAnalyzeFood() {
        val text = binding.etIngredient.text.toString()
        viewModel.analyzeFood(auth.currentUser?.uid.toString(), text).observe(requireActivity()) { result ->
            when(result){
                is ResultState.Loading -> {
                    // Handle loading state
                    binding.rvFood.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvFood.visibility = View.VISIBLE
                    observeGetRecipes()
                }
                is ResultState.Error -> {
                    val errorMessage = result.error
                    // Handle error state
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun observeGetRecipes() {
        viewModel.getRecipes().observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        // Handle loading state
                    }
                    is ResultState.Success -> {
                        val adapter = RecipesAdapter()
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
