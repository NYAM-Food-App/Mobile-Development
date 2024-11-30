package com.example.nyam.view.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.nyam.data.NyamRepository

class CameraViewModel(private val repository: NyamRepository): ViewModel() {

    var currentImageUri: Uri?  = null

}