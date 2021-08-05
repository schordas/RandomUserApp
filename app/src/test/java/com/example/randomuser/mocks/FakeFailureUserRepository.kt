package com.example.randomuser.mocks

import com.example.randomuser.model.User
import com.example.randomuser.network.BaseUserRepository
import com.example.randomuser.network.Result
import com.example.randomuser.util.Constants.repoFailureMessage
import kotlinx.coroutines.flow.flow

class FakeFailureUserRepository : BaseUserRepository {

    override suspend fun getAllUsers() = flow<Result<List<User>>> {
        emit(Result.Failure(repoFailureMessage))
    }

    override suspend fun refreshUsers() = flow<Result<List<User>>> {
        emit(Result.Failure(repoFailureMessage))
    }
}