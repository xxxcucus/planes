package com.planes.android.preferences

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.R
import com.planes.android.Tools

class SpinnerOptionsView(view: View, context: Context) : RecyclerView.ViewHolder(view) {
    var m_Title: TextView
    var m_Text: TextView
    var m_Spinner: Spinner
    var m_Context: Context

    init {
        m_Title = view.findViewById(R.id.title_options)
        m_Text = view.findViewById(R.id.text_options)
        m_Spinner = view.findViewById(R.id.options_spinner)
        m_Context = context
    }

    fun setTitle(title: String) {
        m_Title.setText(title)
    }

    fun setText(text: String) {
        m_Text.setText(text)
    }

    fun setSpinnerOptions(options: ArrayList<String>) {
        val adapter_spinner = ArrayAdapter(m_Context, R.layout.spinner_item, R.id.track_name, options)
        m_Spinner.adapter = adapter_spinner
    }
}