package com.example.wheelchairusers.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wheelchairusers.repository.UserRepository

class UserViewModel : ViewModel() {

    private val repository : UserRepository = UserRepository().getInstance()
    private val _allUsers = MutableLiveData<List<Users>>()
    private val _user = MutableLiveData<Users>()
    val allUsers : LiveData<List<Users>> = _allUsers
    init {
        repository.loadUser(_allUsers)
    }


}