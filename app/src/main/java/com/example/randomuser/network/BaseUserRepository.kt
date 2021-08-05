package com.example.randomuser.network

import com.example.randomuser.model.User
import kotlinx.coroutines.flow.Flow

/**
 * An interface for [UserRepository]
 *
 * This isn't strictly necessary for functionality, but it allows for easy mocking of the repository
 * for unit testing
 */
interface BaseUserRepository {

    suspend fun getAllUsers(): Flow<Result<List<User>>>

    suspend fun refreshUsers(): Flow<Result<List<User>>>
}
