package com.example.randomuser.modules

import android.content.Context
import com.example.randomuser.data.UserDao
import com.example.randomuser.data.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provide all dependencies required to interact with the [UserDao] and [UserDatabase]
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase =
        UserDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideUserDao(database: UserDatabase): UserDao =
        database.userDao()
}
