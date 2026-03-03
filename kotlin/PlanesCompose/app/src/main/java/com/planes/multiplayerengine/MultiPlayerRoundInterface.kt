package com.planes.multiplayerengine

import com.planes.singleplayerengine.PlayerGuessReaction
import com.planes.singleplayerengine.SinglePlayerRoundInterface

interface MultiPlayerRoundInterface : SinglePlayerRoundInterface {

    fun checkRoundEndAsync(): PlayerGuessReaction
}