package com.example.nyam.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.nyam.R
import com.example.nyam.databinding.FragmentOnboardingBinding

class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding

    companion object {
        private const val ARG_POSITION = "ARG_POSITION"

        fun getInstance(position: Int) = OnBoardingFragment().apply {
            arguments = bundleOf(ARG_POSITION to position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(ARG_POSITION)
        val headers: Array<String> = resources.getStringArray(R.array.onboard_header)
        val descriptions: Array<String> = resources.getStringArray(R.array.onboard_desc)
        val previews = getOnBoardPreview()

        with(binding) {
            tvHeader.text = headers[position]
            tvDescription.text = descriptions[position]
            ivPreview.setImageResource(previews[position])
        }

    }


    private fun getOnBoardPreview(): List<Int> {
        val onBoardAssets: MutableList<Int> = ArrayList()
        onBoardAssets.add(R.raw.onboarding_preview1)
        onBoardAssets.add(R.raw.onboarding_preview2)
        onBoardAssets.add(R.raw.onboarding_preview3)
        return onBoardAssets
    }
}