package com.example.randomuser.ui

import com.example.randomuser.network.Result

sealed class UserListViewState<T> {

    class Loading<T>: UserListViewState<T>()
    data class Success<T>(val data: T): UserListViewState<T>()
    data class Failure<T>(val errorMessage: String): UserListViewState<T>()

    companion object {

        fun <T> setLoading() = Loading<T>()

        fun <T> setState(result: Result<T>): UserListViewState<T> {
            return when (result) {
                is Result.Success -> Success(result.data)
                is Result.Failure -> Failure(result.errorMessage)
            }
        }
    }
}