package com.example.nyam.view.updateData

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nyam.R
import com.example.nyam.data.ResultState
import com.example.nyam.data.remote.response.UpdateBody
import com.example.nyam.databinding.ActivityUpdateDataBinding
import com.example.nyam.helper.ViewModelFactory
import com.example.nyam.view.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UpdateDataActivity : AppCompatActivity() {

    private var _binding: ActivityUpdateDataBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val viewmodel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        viewmodel.getUser(firebaseUser!!.uid)

        getUserData()
        supportActionBar?.hide()


        binding.btnUpdate.setOnClickListener {
            updateUserData()
        }
    }

    private fun updateUserData() {
        if (!fieldValidation() ) {
            return
        }
        viewmodel.updateUser(auth.currentUser!!.uid,
            UpdateBody(
                fullname = binding.etName.text.toString(),
                birthdate = binding.etAge.text.toString(),
                gender = binding.spinGender.selectedItemPosition,
                allergy = binding.spinAllergy.selectedAlergy,
                height = binding.etHeight.text.toString().toInt(),
                weight = binding.etWeight.text.toString().toInt(),
            )
        )


        val loadingDialog =
            AlertDialog.Builder(this).setView(R.layout.dialog_builder).create()
        viewmodel.updateResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    loadingDialog.show()
                }

                is ResultState.Success -> {
                    loadingDialog.dismiss()
                    Toast.makeText(this, "Update Berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                }

                is ResultState.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun getUserData() {
        viewmodel.loginResult.observe(this){ result ->
            when(result){
                is ResultState.Loading -> {
                }
                is ResultState.Success -> {
                    with(result.data){
                        binding.etName.setText(fullname)
                        binding.etAge.setText(birthdate)
                        binding.spinGender.id = gender?:0
                        binding.etWeight.setText(weight.toString())
                        binding.etHeight.setText(height.toString())
                    }
                }
                is ResultState.Error -> {
                    Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fieldValidation(): Boolean {

        with(binding) {
            if (etName.text.isNullOrBlank() or etName.text.isNullOrEmpty() or
                etAge.text.isNullOrBlank() or etAge.text.isNullOrEmpty() or
                etHeight.text.isNullOrBlank() or etHeight.text.isNullOrEmpty() or
                etWeight.text.isNullOrBlank() or etWeight.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    this@UpdateDataActivity,
                    "Please fill all the field",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }
}