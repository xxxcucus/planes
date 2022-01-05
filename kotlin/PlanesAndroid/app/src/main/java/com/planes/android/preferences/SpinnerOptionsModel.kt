package com.planes.android.preferences

class SpinnerOptionsModel(title: String, text: String, spinnerOptions: ArrayList<String>): BasisOptionsModel(title, text) {

    private lateinit var m_SpinnerOptions: ArrayList<String>

    init {
        m_SpinnerOptions = spinnerOptions
    }

    fun getSpinnerOptions(): ArrayList<String> {
        return m_SpinnerOptions
    }
}