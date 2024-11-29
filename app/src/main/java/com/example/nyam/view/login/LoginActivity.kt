package com.example.nyam.view.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nyam.MainActivity
import com.example.nyam.view.personal.PersonalDataActivity
import com.example.nyam.R
import com.example.nyam.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        binding.Signin.setOnClickListener{
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
        binding.loginToRegister.setOnClickListener{
            val intent = Intent(baseContext, PersonalDataActivity::class.java)
            startActivity(intent)
        }
    }
}