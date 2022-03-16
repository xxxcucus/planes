package com.planes.android.videos

import com.planes.android.preferences.IPreferencesService

interface IVideoSettingsService : IPreferencesService {
    var currentVideo: Int
    var videoPlaybackPositions: IntArray
}