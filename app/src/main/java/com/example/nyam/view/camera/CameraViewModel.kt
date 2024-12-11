package com.example.nyam.view.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.nyam.data.NyamRepository
import java.io.File

class CameraViewModel(private val repository: NyamRepository): ViewModel() {

    var currentImageUri: Uri?  = null

    fun analyzeImage(id :String, file: File) = repository.analyzeImage(id,file)

}