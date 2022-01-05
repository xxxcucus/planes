package com.planes.android.preferences

class EditTextOptionsModel(title: String, text: String): BasisOptionsModel(title, text) {

    private var m_EditableText = ""

    fun getEditableText(): String {
        return m_EditableText
    }
}