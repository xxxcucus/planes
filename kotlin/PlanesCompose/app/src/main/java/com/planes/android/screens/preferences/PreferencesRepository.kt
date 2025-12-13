package com.planes.android.screens.preferences

import androidx.datastore.core.DataStore
import java.util.prefs.Preferences
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
)