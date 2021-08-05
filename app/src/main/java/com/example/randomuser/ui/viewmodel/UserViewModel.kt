package com.example.randomuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuser.model.User
import com.example.randomuser.network.BaseUserRepository
import com.example.randomuser.ui.UserListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: BaseUserRepository
): ViewModel() {

    private val _state = MutableLiveData<UserListViewState<List<User>>>()
    val state: LiveData<UserListViewState<List<User>>> = _state

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            userRepository.getAllUsers()
                .flowOn(Dispatchers.IO)
                .onStart { _state.value = UserListViewState.setLoading() }
                .map { result ->
                    UserListViewState.setState(result)
                }
                .collect { state ->
                    _state.value = state
                }
        }
    }

    fun refreshUsers() {
        viewModelScope.launch {
            userRepository.refreshUsers()
                .flowOn(Dispatchers.IO)
                .onStart { _state.value = UserListViewState.setLoading() }
                .map { result ->
                    UserListViewState.setState(result)
                }
                .collect { state ->
                    _state.value = state
                }
        }
    }
}
