package com.example.randomuser.ui

import com.example.randomuser.network.Result

/**
 * Sealed class for the view state that is handled by the [UserListFragment]
 *
 * The state is set according to the emission of the resulting flow from the repository.
 * The state is set to loading after repository request but before flow emission.
 */
sealed class UserListViewState<T> {

    class Loading<T>: UserListViewState<T>()
    data class Success<T>(val data: T): UserListViewState<T>()
    data class Failure<T>(val errorMessage: String): UserListViewState<T>()

    companion object {

        // Set state to loading before result has been emitted
        fun <T> setLoading() = Loading<T>()

        // Set state by result
        fun <T> setState(result: Result<T>): UserListViewState<T> {
            return when (result) {
                is Result.Success -> Success(result.data)
                is Result.Failure -> Failure(result.errorMessage)
            }
        }
    }
}
