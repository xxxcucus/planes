package com.planes.android.register

import android.os.Bundle

interface INotPersistingPreferencesService {
    fun readFromSavedInstanceState(savedInstanceState: Bundle)
    fun writeToSavedInstanceState(savedInstanceState: Bundle)
}