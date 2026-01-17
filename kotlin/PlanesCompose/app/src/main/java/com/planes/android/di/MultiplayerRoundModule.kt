package com.planes.android.di

import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.multiplayerengine.MultiplayerRound
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MultiplayerRoundModule {

    @Provides
    @Singleton
    fun provideMultiplayerRound(factory: MultiplayerRound.Factory): MultiPlayerRoundInterface {
        return factory.createPlaneRound(10, 10, 3)
    }
}