package com.planes.singleplayerengine

//plane orientation
enum class Orientation(val value: Int) {
    NorthSouth(0), SouthNorth(1), WestEast(2), EastWest(3);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}