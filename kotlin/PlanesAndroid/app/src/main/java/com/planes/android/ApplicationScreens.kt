package com.planes.android

enum class ApplicationScreens(val value: Int) {
    Preferences(0), Game(1), Videos(2), About(3),
    Login(4), Logout(5), Register(6), NoRobot(7),
    CreateGame(8), GameStats(9), DeleteUser(10), Chat(11);

    companion object {
        private val map = ApplicationScreens.values().associateBy(ApplicationScreens::value)
        operator fun get(value: Int) = map[value]
    }
}