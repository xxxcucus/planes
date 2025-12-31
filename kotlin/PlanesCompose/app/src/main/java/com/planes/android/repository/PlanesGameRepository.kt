package com.planes.android.repository

import com.planes.android.network.game.PlanesGameApi
import com.planes.android.network.user.PlanesUserApi
import javax.inject.Inject

class PlanesGameRepository @Inject constructor(private val api: PlanesGameApi) {
}