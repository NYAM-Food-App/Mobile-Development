package com.example.nyam.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.nyam.MainActivity
import com.example.nyam.R
import com.example.nyam.databinding.ActivityLoginBinding
import com.example.nyam.helper.ViewModelFactory
import com.example.nyam.view.MainViewModel
import com.example.nyam.view.register.RegisterDataActivity
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

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

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
        auth = Firebase.auth

        supportActionBar?.hide()

        binding.Signin.setOnClickListener {
            signIn()
        }
        binding.loginToRegister.setOnClickListener {
            goToRegister()
        }
    }

    private fun goToRegister(){
        val intent = Intent(baseContext, RegisterDataActivity::class.java)
        startActivity(intent)
    }

    private fun signIn() {
        val credentialManager =
            CredentialManager.create(this) //import from androidx.CredentialManager

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)

            .setServerClientId(getString(R.string.your_web_client_id)) //from https://console.firebase.google.com/project/firebaseProjectName/authentication/providers
            .build()


        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
            .addCredentialOption(googleIdOption)
            .build()

        Log.d(TAG, "signIn: Request: $request")

        lifecycleScope.launch {

            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    //import from androidx.CredentialManager
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e:GetCredentialCancellationException){
                return@launch
            }
            catch (e: GetCredentialException) { //import from androidx.CredentialManager
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

                    updateUI(user)
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
        private const val TAG = "LoginActivity"
    }
}