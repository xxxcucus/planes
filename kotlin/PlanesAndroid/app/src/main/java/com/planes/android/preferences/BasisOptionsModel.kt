package com.planes.android.preferences

open class BasisOptionsModel(title: String, text: String) {
    protected lateinit var m_Title: String
    protected lateinit var m_Text: String

    init {
        m_Title = title
        m_Text = text
    }

    fun getTitle(): String {
        return m_Title
    }

    fun getText(): String {
        return m_Text
    }
}