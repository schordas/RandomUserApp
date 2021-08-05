package com.example.randomuser.network

import com.example.randomuser.model.User
import kotlinx.coroutines.flow.Flow

interface BaseUserRepository {

    suspend fun getAllUsers(): Flow<Result<List<User>>>

    suspend fun refreshUsers(): Flow<Result<List<User>>>
}