package com.planes.android.di

import android.content.Context
import com.planes.android.screens.video.VideoModelRepository
import com.planes.android.screens.video.VideoModelRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class VideoRepositoryModule {
    @Provides
    @Singleton
    fun provideVideoRepository(@ApplicationContext context: Context): VideoModelRepositoryInterface {
        val videoRepository = VideoModelRepository()
        videoRepository.create(context)
        return videoRepository
    }
}