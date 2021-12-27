package com.planes.android.preferences
import android.os.Bundle


interface IPreferencesService {

    var computerSkill: Int
    var showPlaneAfterKill: Boolean

    fun readPreferences()
    fun readFromSavedInstanceState(savedInstanceState: Bundle)
    fun writePreferences()
    fun writeToSavedInstanceState(savedInstanceState: Bundle)
}