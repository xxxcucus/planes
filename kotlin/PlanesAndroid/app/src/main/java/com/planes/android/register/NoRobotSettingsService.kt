package com.planes.android.register

import android.os.Bundle

class NoRobotSettingsService: INoRobotSettingsService {

    override var requestId: String = "0"
    override  var images: Array<String> = arrayOf("", "", "", "", "", "", "", "", "")
    override  var question: String = ""
    override  var selection: Array<Boolean> = arrayOf(false, false, false, false, false, false, false, false, false)

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