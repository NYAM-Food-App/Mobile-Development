package com.example.nyam.view.result

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nyam.BaseClass
import com.example.nyam.MainActivity
import com.example.nyam.R
import com.example.nyam.databinding.ActivityResultSuccessBinding
import com.example.nyam.view.onboarding.OnBoardingActivity

class ResultActivity : BaseClass(false) {
    private lateinit var _binding: ActivityResultSuccessBinding
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityResultSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

    }
}