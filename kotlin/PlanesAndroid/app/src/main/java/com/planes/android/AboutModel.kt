package com.planes.android

class AboutModel(title: String, text: String, buttonPresent: Boolean, textButton: String, linkButton: String) {

    private var m_Title: String
    private var m_Text: String
    private var m_ButtonPresent: Boolean
    private var m_TextButton: String
    private var m_LinkButton: String

    init {
        m_Title = title
        m_Text = text
        m_ButtonPresent = buttonPresent
        m_TextButton = textButton
        m_LinkButton = linkButton
    }

    fun getTitle(): String {
        return m_Title
    }

    fun getText(): String {
        return m_Text
    }

    fun getButtonPresent(): Boolean {
        return m_ButtonPresent
    }

    fun getTextButton(): String {
        return m_TextButton
    }

    fun getLinkButton(): String {
        return m_LinkButton
    }
}