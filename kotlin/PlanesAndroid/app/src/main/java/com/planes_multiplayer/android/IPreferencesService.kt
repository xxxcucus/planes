package com.planes.android
import android.os.Bundle


interface IPreferencesService {

    var computerSkill: Int
    var showPlaneAfterKill: Boolean

    fun readPreferences()
    fun readFromSavedInstanceState(savedInstanceState: Bundle)
    fun writePreferences()
    fun writeToSavedInstanceState(savedInstanceState: Bundle)
}