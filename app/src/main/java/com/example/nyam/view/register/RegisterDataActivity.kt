package com.example.nyam.view.register

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.nyam.MainActivity
import com.example.nyam.R
import com.example.nyam.data.ResultState
import com.example.nyam.data.remote.response.RegisterBody
import com.example.nyam.databinding.ActivityRegisterDataBinding
import com.example.nyam.helper.ViewModelFactory
import com.example.nyam.view.MainViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterDataActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityRegisterDataBinding
    private val binding get() = _binding

    private lateinit var auth: FirebaseAuth

    private val calendar = Calendar.getInstance()

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        _binding = ActivityRegisterDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnRegister.setOnClickListener {
            register()
        }

        binding.etAge.setOnClickListener {
            showDatePicker()
        }

    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.etAge.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    /*
    Returns false if required field is not filled
     */
    private fun fieldValidation(): Boolean {

        with(binding) {
            if (etName.text.isNullOrBlank() or etName.text.isNullOrEmpty() or
                etAge.text.isNullOrBlank() or etAge.text.isNullOrEmpty() or
                etHeight.text.isNullOrBlank() or etHeight.text.isNullOrEmpty() or
                etWeight.text.isNullOrBlank() or etWeight.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    this@RegisterDataActivity,
                    "Please fill all the field",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }

    private fun register() {
        if (!fieldValidation() ) {
            return
        }

        val credentialManager =
            CredentialManager.create(this) //import from androidx.CredentialManager

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.your_web_client_id)) //from https://console.firebase.google.com/project/firebaseProjectName/authentication/providers
            .build()


        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {

            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    //import from androidx.CredentialManager
                    request = request,
                    context = this@RegisterDataActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) { //import from androidx.CredentialManager
                Log.d("Error", e.message.toString())
            }
        }
    }


    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and authenticate on your server.
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser

                    with(binding) {
                        val fullName = etName.text.toString()
                        val gender = spinGender.selectedItemPosition
                        val age = etAge.text.toString()
                        val allergy = spinAllergy.selectedAlergy
                        val height = etHeight.text.toString()
                        val weight = etWeight.text.toString()
                        viewModel.registerUser(
                            RegisterBody(
                                uid = user?.uid.toString(),
                                fullname = fullName,
                                birthdate = age,
                                gender = gender,
                                allergy = allergy.toList(),
                                height = height.toInt(),
                                weight = weight.toInt(),
                            )
                        )
                    }

                    val loadingDialog =
                        AlertDialog.Builder(this).setView(R.layout.dialog_builder).create()
                    viewModel.registerResult.observe(this) { result ->
                        when (result) {
                            is ResultState.Loading -> {
                                loadingDialog.show()
                            }

                            is ResultState.Success -> {
                                loadingDialog.dismiss()
                                updateUI(user)
                            }

                            is ResultState.Error -> {
                                loadingDialog.dismiss()
                                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                                updateUI(null)
                            }
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}