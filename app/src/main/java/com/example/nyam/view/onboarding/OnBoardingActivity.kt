package com.example.nyam.view.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.nyam.R
import com.example.nyam.adapter.OnBoardingAdapter
import com.example.nyam.databinding.ActivityOnboardingBinding
import com.example.nyam.view.login.LoginActivity

class OnBoardingActivity : AppCompatActivity() {

    private var onBoardingPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            changeCircleMark(position)
        }
    }

    private var _binding: ActivityOnboardingBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        binding.btnSkip.setOnClickListener {
            val intent = Intent(this.baseContext, LoginActivity::class.java)
            startActivity(intent)
}

        val numberOfScreens = resources.getStringArray(R.array.onboard_header).size
        val onBoardingAdapter = OnBoardingAdapter(this, numberOfScreens)
        binding.onboardingViewPager.adapter = onBoardingAdapter
        binding.onboardingViewPager.registerOnPageChangeCallback(onBoardingPageChangeCallback)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changeCircleMark(position: Int) {
        when (position) {
            0 -> {
                binding.onboardingInitialCircle.background = getDrawable(R.drawable.circle_selected)
                binding.onboardingMiddleCircle.background = getDrawable(R.drawable.circle_gray)
                binding.onboardingLastCircle.background = getDrawable(R.drawable.circle_gray)
            }

            1 -> {
                binding.onboardingInitialCircle.background = getDrawable(R.drawable.circle_gray)
                binding.onboardingMiddleCircle.background = getDrawable(R.drawable.circle_selected)
                binding.onboardingLastCircle.background = getDrawable(R.drawable.circle_gray)
            }

            2 -> {
                binding.onboardingInitialCircle.background = getDrawable(R.drawable.circle_gray)
                binding.onboardingMiddleCircle.background = getDrawable(R.drawable.circle_gray)
                binding.onboardingLastCircle.background = getDrawable(R.drawable.circle_selected)
            }
        }
    }

    override fun onDestroy() {
        binding.onboardingViewPager.unregisterOnPageChangeCallback(onBoardingPageChangeCallback)
        super.onDestroy()
    }
}