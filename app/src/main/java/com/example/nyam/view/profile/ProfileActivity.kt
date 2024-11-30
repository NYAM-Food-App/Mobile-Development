package com.example.nyam.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nyam.R
import com.example.nyam.databinding.ActivityProfileBinding
import com.example.nyam.helper.ViewModelFactory
import com.example.nyam.view.MainViewModel
import com.example.nyam.view.onboarding.OnBoardingActivity

class ProfileActivity : AppCompatActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    private val viewmodel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            viewmodel.logout()
            intent = Intent(this,OnBoardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}