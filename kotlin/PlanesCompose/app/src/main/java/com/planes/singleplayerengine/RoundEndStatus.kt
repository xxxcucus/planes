package com.planes.singleplayerengine

enum class RoundEndStatus(val value: Int) {
    PlayerWins(0), ComputerWins(1), Draw(2), Cancelled(3);
}