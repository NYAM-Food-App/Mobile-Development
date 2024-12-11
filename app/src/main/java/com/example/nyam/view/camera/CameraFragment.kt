package com.example.nyam.view.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nyam.R
import com.example.nyam.data.ResultState
import com.example.nyam.databinding.FragmentCameraBinding
import com.example.nyam.helper.ViewModelFactory
import com.example.nyam.helper.reduceFileImage
import com.example.nyam.helper.uriToFile
import com.example.nyam.view.camera.CameraXActivity.Companion.CAMERAX_RESULT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<CameraViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        requireContext(), REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        auth = Firebase.auth

        showImage()
        with(binding) {
            btnUpload.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCameraX() }
            btnSearch.setOnClickListener { startSearch() }
        }
    }

    private fun startSearch() {
        uploadImage()
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraXActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            viewModel.currentImageUri = it.data?.getStringExtra(CameraXActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }
    private fun showImage() {
        currentImageUri = viewModel.currentImageUri
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPhoto.setImageURI(it)
        }
    }


    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val loadingDialog = AlertDialog.Builder(requireContext()).setView(R.layout.dialog_builder).create()

            auth.currentUser?.uid?.let {
                viewModel.analyzeImage(it,imageFile).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> {
                                loadingDialog.show()
                            }

                            is ResultState.Success -> {
                                showToast("${result.data.message} : ${result.data.foodPrediction.predictedClass}")
                                loadingDialog.dismiss()
                                findNavController().navigate(R.id.action_navigation_camera_to_recommendActivity)
                            }

                            is ResultState.Error -> {
                                showToast(result.error)
                                loadingDialog.dismiss()
                            }
                        }
                    }
                }
            }

        } ?:showToast(getString(R.string.empty_image))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    companion object {
        const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}