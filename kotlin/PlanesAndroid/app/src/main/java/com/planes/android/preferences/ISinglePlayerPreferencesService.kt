package com.planes.android.preferences

interface ISinglePlayerPreferencesService: IPreferencesService {
    var computerSkill: Int
    var showPlaneAfterKill: Boolean
}