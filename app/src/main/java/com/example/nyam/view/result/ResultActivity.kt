package com.example.nyam.view.result

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.nyam.BaseClass
import com.example.nyam.MainActivity
import com.example.nyam.databinding.ActivityResultSuccessBinding

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