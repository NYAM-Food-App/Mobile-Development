package com.example.nyam.view.history

import androidx.lifecycle.ViewModel
import com.example.nyam.data.NyamRepository

class HistoryViewModel(private val repository: NyamRepository):ViewModel() {

    fun getHistory(id:String) =repository.getHistory(id)

}