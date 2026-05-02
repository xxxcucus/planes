package com.planes.android.di

import android.content.Context
import androidx.room.Room
import com.planes.android.data.ChatDao
import com.planes.android.data.NewMessagesDao
import com.planes.android.data.PlanesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {


    @Provides
    @Singleton
    fun provideChatDao(planesDatabase: PlanesDatabase): ChatDao =
        planesDatabase.chatDao()

    @Provides
    @Singleton
    fun provideNewMessagesDao(planesDatabase: PlanesDatabase): NewMessagesDao =
        planesDatabase.newMessagesDao()

    @Singleton
    @Provides
    fun providePlanesDatabase(@ApplicationContext context: Context): PlanesDatabase =
        Room.databaseBuilder(
                context,
                PlanesDatabase::class.java,
                "planes.db"
            ).fallbackToDestructiveMigration(false).build()

}