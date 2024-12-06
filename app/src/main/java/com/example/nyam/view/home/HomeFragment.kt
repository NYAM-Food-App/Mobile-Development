package com.example.nyam.view.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nyam.R
import com.example.nyam.data.ResultState
import com.example.nyam.databinding.FragmentHomeBinding
import com.example.nyam.helper.ViewModelFactory
import com.example.nyam.helper.getPercent

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var colorList:List<ColorStateList?>

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Tes ProgressBar onClick aja, bisa dihapus
        binding.cardDailyReport.progressBarCalories.setOnClickListener {
            binding.cardDailyReport.progressBarCalories.progress += 10
        }

        colorList = listOf(
            ContextCompat.getColorStateList(requireContext(),R.color.extreme_under),
            ContextCompat.getColorStateList(requireContext(),R.color.under),
            ContextCompat.getColorStateList(requireContext(),R.color.normal),
            ContextCompat.getColorStateList(requireContext(),R.color.overweight),
            ContextCompat.getColorStateList(requireContext(),R.color.obesity),
            ContextCompat.getColorStateList(requireContext(),R.color.extreme_obesity)
        )

        viewModel.getUser()
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.tvBmi.text = "Loading"
                }

                is ResultState.Success -> {
                    binding.tvBmi.text = "BMI Selesai"

                    with(result.data) {
                        val cal = "${fulfilledNeeds.calories}/${dailyNeeds.calories}"
                        val calPercent = getPercent(fulfilledNeeds.calories as Double,dailyNeeds.calories)
                        val calProgress = (fulfilledNeeds.calories / dailyNeeds.calories * 100).toInt()

                        val fat = "${fulfilledNeeds.fat}/${dailyNeeds.fat}"
                        val fatPercent = getPercent(fulfilledNeeds.fat as Double,dailyNeeds.fat)
                        val fatProgress = (fulfilledNeeds.fat / dailyNeeds.fat * 100).toInt()

                        val carbs = "${fulfilledNeeds.carbs}/${dailyNeeds.carbs}"
                        val carbsPercent = getPercent(fulfilledNeeds.carbs as Double,dailyNeeds.carbs)
                        val carbsProgress = (fulfilledNeeds.carbs / dailyNeeds.carbs * 100).toInt()

                        val protein = "${fulfilledNeeds.protein}/${dailyNeeds.protein}"
                        val proteinPercent = getPercent(fulfilledNeeds.protein as Double,dailyNeeds.protein)
                        val proteinProgress = (fulfilledNeeds.protein / dailyNeeds.protein * 100).toInt()


                        with(binding.cardDailyReport) {
                            valuesCalories.text = cal
                            percentCalories.text = calPercent
                            progressBarCalories.progress = calProgress

                            valuesFat.text = fat
                            percentFat.text = fatPercent
                            progressBarFat.progress = fatProgress

                            valuesCarbs.text = carbs
                            percentCarbs.text = carbsPercent
                            progressBarCarbs.progress = carbsProgress

                            valuesProtein.text = protein
                            percentProtein.text = proteinPercent
                            progressBarProtein.progress = proteinProgress
                        }

                        with(binding.cardBmi){
                            valuesHeight.text = height.toString()
                            valuesWeight.text = weight.toString()
                            valuesBmi.text = String.format((bmi).toString())

                            ivBmiIndex.backgroundTintList = colorList[bmrRate]
                        }
                    }
                }
                is ResultState.Error -> {
                    Toast.makeText(requireContext(), "gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}