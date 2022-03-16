package com.planes.android.register

import android.os.Bundle

class NoRobotSettingsService: INoRobotSettingsService {

    override lateinit var requestId: String
    override lateinit var images: Array<String>
    override lateinit var question: String

    override fun readFromSavedInstanceState(savedInstanceState: Bundle) {
        requestId = savedInstanceState.getString("norobot/requestid")!!
        images = savedInstanceState.getSerializable("norobot/images") as Array<String>
        question = savedInstanceState.getString("norobot/question")!!
    }

    override fun writeToSavedInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString("norobot/requestid", requestId)
        savedInstanceState.putSerializable("norobot/images", images)
        savedInstanceState.putString("norobot/question", question)
    }
}