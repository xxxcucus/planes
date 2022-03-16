package com.planes.android.register

interface INoRobotSettingsService : INotPersistingPreferencesService {
    var requestId: String
    var images: Array<String>
    var question: String
}