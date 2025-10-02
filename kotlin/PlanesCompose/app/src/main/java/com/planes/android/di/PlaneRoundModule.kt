package com.planes.android.di

import com.planes.singleplayerengine.PlaneRound
import com.planes.singleplayerengine.PlanesRoundInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PlaneRoundModule {

    @Provides
    fun providePlaneRound(factory: PlaneRound.Factory): PlanesRoundInterface {
        return factory.createPlaneRound(10, 10, 3)
    }
}