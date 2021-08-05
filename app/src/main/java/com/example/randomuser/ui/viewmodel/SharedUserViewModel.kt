package com.example.randomuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.randomuser.model.User

class SharedUserViewModel: ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun onUserClicked(user: User) {
        _user.value = user
    }
}
