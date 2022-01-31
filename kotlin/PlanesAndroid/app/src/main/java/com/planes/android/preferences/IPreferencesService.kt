package com.planes.android.preferences
import android.os.Bundle


interface IPreferencesService {

    fun readPreferences()
    fun readFromSavedInstanceState(savedInstanceState: Bundle)
    fun writePreferences()
    fun writeToSavedInstanceState(savedInstanceState: Bundle)
}