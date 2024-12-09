package com.example.nyam.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.example.nyam.MainActivity
import com.example.nyam.R
import com.example.nyam.databinding.ActivityProfileBinding
import com.example.nyam.helper.ViewModelFactory
import com.example.nyam.view.MainViewModel
import com.example.nyam.view.login.LoginActivity
import com.example.nyam.view.onboarding.OnBoardingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private val viewmodel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        binding.btnLogout.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@ProfileActivity)

            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            val intent = Intent(this@ProfileActivity, OnBoardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}