package com.planes.android.preferences

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.R

class EditTextOptionsView(view: View, context: Context) : RecyclerView.ViewHolder(view) {
    var m_Title: TextView
    var m_Text: TextView
    var m_EditText: EditText
    var m_Context: Context

    init {
        m_Title = view.findViewById(R.id.title_options)
        m_Text = view.findViewById(R.id.text_options)
        m_EditText = view.findViewById(R.id.options_edittext)
        m_Context = context
    }

    fun setTitle(title: String) {
        m_Title.setText(title)
    }

    fun setText(text: String) {
        m_Text.setText(text)
    }

    fun setEditableText(text: String) {
        m_EditText.setText(text)
    }

    //TODO: password protection
}
