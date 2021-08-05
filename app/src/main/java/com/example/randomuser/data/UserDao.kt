package com.example.randomuser.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.randomuser.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user_database")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT count(*) FROM user_database")
    fun getUserCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUsers(users: List<User>)

    // Delete annotation requires an entity, query allows for a full database wipe
    @Query("DELETE FROM user_database")
    suspend fun deleteAllUsers()
}
