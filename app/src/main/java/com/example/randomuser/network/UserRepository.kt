package com.example.randomuser.network

import android.util.Log
import com.example.randomuser.data.UserDao
import com.example.randomuser.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val userDao: UserDao
): BaseUserRepository {

    private val TAG = UserRepository::class.qualifiedName

    override suspend fun getAllUsers() = flow<Result<List<User>>> {
        // If database is not empty, emit data from the database
        // The reason for this approach is discussed in detail in the README
        val userCount = getUserCountFromDB()
        if (userCount > 0) { // Only emit data from the database if the database is not empty
            emit(Result.Success(getAllUsersFromDB().first()))
        } else { // If the database is empty make a network request
            try {
                val response = getUsersFromApi(50)
                val users = response.body()
                if (response.isSuccessful && users != null) {
                    emit(Result.Success(users))
                    saveUsersToDB(users)
                } else {
                    emit(Result.Failure(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                // In practice this would log to a Third Party API for crash analytics
                Log.e(TAG, "Failure in getAllUsers(): ${e.message.toString()}")
                emit(Result.Failure(e.message.toString()))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun refreshUsers() = flow<Result<List<User>>> {
        try {
            val response = getUsersFromApi(50)
            val users = response.body()
            if (response.isSuccessful && users != null && users.isNotEmpty()) {
                emit(Result.Success(users))
                // Once data has been emitted update the database. Delete old users.
                deleteAllUsers()
                // Save new users
                saveUsersToDB(users)
            } else {
                emit(Result.Failure(response.errorBody().toString()))
            }
        } catch (e: Exception) {
            // In practice this would log to a Third Party API for crash analytics
            Log.e(TAG, "Failure in refreshUsers(): ${e.message.toString()}")
            emit(Result.Failure(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    private fun getAllUsersFromDB(): Flow<List<User>> = userDao.getAllUsers()

    private fun getUserCountFromDB(): Int = userDao.getUserCount()

    private suspend fun getUsersFromApi(size: Int): Response<List<User>> =
        userApiService.getUsers(size)

    private suspend fun saveUsersToDB(data: List<User>) {
        userDao.addUsers(data)
    }

    private suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }
}

/**
 * A class to hand the state of the network/database request.
 * In this class it's mostly used for the network request
 */
sealed class Result<T> {
    class Success<T>(val data: T): Result<T>()
    class Failure<T>(val errorMessage: String): Result<T>()
}
