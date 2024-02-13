package com.example.wheelchairusers.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wheelchairusers.repository.HelpRepository

class HelpViewModel: ViewModel() {

    private val repository : HelpRepository = HelpRepository().getInstance()
    private val _allHelps = MutableLiveData<List<Help>>()
    val allHelps : LiveData<List<Help>> = _allHelps
    init {
        repository.loadHelp(_allHelps)
    }

}