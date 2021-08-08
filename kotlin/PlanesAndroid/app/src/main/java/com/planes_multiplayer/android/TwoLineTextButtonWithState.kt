package com.planes_multiplayer.android

import android.content.Context
import android.util.AttributeSet

class TwoLineTextButtonWithState : TwoLineTextButton, ObjectWithStringState, ViewWithText {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    override fun setState(stateName: String, text: String?) {
        //TODO: read texts from resources
        currentStateName = stateName
        if (currentStateName === "player") {
            m_Text2 = text
        }
        if (currentStateName === "computer") {
            m_Text2 = text
        }
        invalidate()
    }

    override var currentStateName: String? = null
}