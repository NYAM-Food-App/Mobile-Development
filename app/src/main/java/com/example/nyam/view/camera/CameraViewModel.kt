package com.example.nyam.view.camera

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nyam.data.NyamRepository
import com.example.nyam.data.ResultState
import com.example.nyam.data.remote.response.AnalyzeResponse
import com.example.nyam.data.remote.response.RecipesItem
import java.io.File

class CameraViewModel(private val repository: NyamRepository): ViewModel() {

    var currentImageUri: Uri?  = null

    fun uploadImage(id :String,file: File) = repository.uploadImage(id,file)

}