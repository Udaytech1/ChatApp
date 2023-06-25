package com.chatapp.compose.data.di

import com.chatapp.compose.utils.FirebaseConstants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideDatabaseRef(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(FirebaseConstants.DatabaseName)
    }
}