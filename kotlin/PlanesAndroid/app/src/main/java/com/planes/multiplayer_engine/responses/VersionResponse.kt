package com.planes.multiplayer_engine.responses

class VersionResponse(VersionString: String) {
    var versionString: String

    init {
        versionString = VersionString
    }
}