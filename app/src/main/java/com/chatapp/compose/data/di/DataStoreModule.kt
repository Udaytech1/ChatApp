package com.chatapp.compose.data.di

import android.content.Context
import com.chatapp.compose.utils.DatastoreHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provoideDataStore(@ApplicationContext context: Context): DatastoreHelper {
        return DatastoreHelper(context)
    }
}