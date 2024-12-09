package com.example.nyam.view.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.nyam.data.ResultState
import com.example.nyam.databinding.ActivityProfileBinding
import com.example.nyam.helper.ViewModelFactory
import com.example.nyam.view.MainViewModel
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
        viewmodel.getUser(firebaseUser?.uid.toString())

        changeImage()
        getUserData()

        with(binding){

            btnLogout.setOnClickListener {
                signOut()
            }
            btnEditData.setOnClickListener{
                goToEdit()
            }
        }
    }

    private fun goToEdit() {
        intent = Intent(this)
    }

    private fun getUserData() {
        viewmodel.loginResult.observe(this){ result ->
            when(result){
                is ResultState.Loading -> {

                }
                is ResultState.Success -> {
                    with(result.data){
                        binding.tvName.text = fullname
                        binding.tvEmail.text = email
                        binding.tvAge.text = birthdate
                        if(gender == 0){ binding.tvGender.text = "Male" } else { binding.tvGender.text = "Female" }
                        binding.tvAllergy.text = allergy.toString().first().uppercase()
                    }
                }
                is ResultState.Error -> {
                    Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun changeImage() {
        Glide.with(this)
            .load(auth.currentUser?.photoUrl)
            .circleCrop()
            .into(binding.ivProfile)
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