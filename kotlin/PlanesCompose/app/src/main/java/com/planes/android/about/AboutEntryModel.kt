package com.planes.android.about

class AboutEntryModel(title: String, text: String,
                      buttonPresent: Boolean, textButton: String,
                      linkButton: String) {

    private var m_Title: String = title
    private var m_Text: String = text
    private var m_ButtonPresent: Boolean = buttonPresent
    private var m_TextButton: String = textButton
    private var m_LinkButton: String = linkButton

    fun getTitle(): String {
        return m_Title
    }

    fun getText(): String {
        return m_Text
    }

    fun getTextButton(): String {
        return m_TextButton
    }

    fun hasButton(): Boolean {
        return m_ButtonPresent;
    }

    fun getLinkButton(): String {
        return m_LinkButton
    }
}