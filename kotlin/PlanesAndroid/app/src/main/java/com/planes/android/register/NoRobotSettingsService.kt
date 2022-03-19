package com.planes.android.register

import android.os.Bundle

class NoRobotSettingsService: INoRobotSettingsService {

    override lateinit var requestId: String
    override lateinit var images: Array<String>
    override lateinit var question: String
    override lateinit var selection: Array<Boolean>

    override fun readFromSavedInstanceState(savedInstanceState: Bundle) {
        requestId = savedInstanceState.getString("norobot/requestid")!!
        images = savedInstanceState.getSerializable("norobot/images") as Array<String>
        question = savedInstanceState.getString("norobot/question")!!
        selection = savedInstanceState.getSerializable("norobot/selection") as Array<Boolean>
    }

    override fun writeToSavedInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString("norobot/requestid", requestId)
        savedInstanceState.putSerializable("norobot/images", images)
        savedInstanceState.putString("norobot/question", question)
        savedInstanceState.putSerializable("norobot/selection", selection)
    }
}