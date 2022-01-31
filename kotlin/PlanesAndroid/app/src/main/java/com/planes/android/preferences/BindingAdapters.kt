package com.planes.android.preferences

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.planes.android.R

object BindingAdapters {

    @BindingAdapter(value = ["currentSkill", "currentSkillAttrChanged"], requireAll = false)
    @JvmStatic fun setCurrentSkill(spinner: Spinner, currentSkill: Int, listener: InverseBindingListener) {
        val adapter_skill = ArrayAdapter.createFromResource(
            spinner.context,
            R.array.computer_skills, R.layout.spinner_item
        )
        spinner.adapter = adapter_skill
        setCurrentSelection(spinner, currentSkill)
        setSpinnerListener(spinner, listener)
    }

    @InverseBindingAdapter(attribute = "currentSkill")
    @JvmStatic fun getCurrentSkill(spinner: Spinner): Int {
        return spinner.selectedItemPosition
    }

    @BindingAdapter(value = ["showPlaneAfterKill", "showPlaneAfterKillAttrChanged"], requireAll = false)
    @JvmStatic fun setShowPlaneAfterKill(spinner: Spinner, showPlaneAfterKill: Boolean, listener: InverseBindingListener) {
        setYesNoSpinnerForBoolean(spinner, showPlaneAfterKill, listener)
    }

    @BindingAdapter(value = ["multiplayerVersion", "multiplayerVersionAttrChanged"], requireAll = false)
    @JvmStatic fun setMultiplayerVersion(spinner: Spinner, multiplayerVersion: Boolean, listener: InverseBindingListener) {
        setYesNoSpinnerForBoolean(spinner, multiplayerVersion, listener)
    }

    @InverseBindingAdapter(attribute = "showPlaneAfterKill")
    @JvmStatic fun getShowPlaneAfterKill(spinner: Spinner): Boolean {
        return getYesNoSpinnerForBoolean(spinner)
    }

    @InverseBindingAdapter(attribute = "multiplayerVersion")
    @JvmStatic fun getMultiplayerVersion(spinner: Spinner): Boolean {
        return getYesNoSpinnerForBoolean(spinner)
    }

    private fun getYesNoSpinnerForBoolean(spinner: Spinner): Boolean {
        return if (spinner.selectedItemPosition == 0) true else false
    }

    private fun setYesNoSpinnerForBoolean(spinner: Spinner, flag: Boolean, listener: InverseBindingListener) {
        val adapter = ArrayAdapter.createFromResource(
            spinner.context,
            R.array.yesno_options, R.layout.spinner_item
        )
        spinner.adapter = adapter
        setCurrentSelection(spinner, if (flag)  0 else 1)
        setSpinnerListener(spinner, listener)
    }

    private fun setCurrentSelection(spinner: Spinner, selectedPosition: Int): Boolean {
        if (selectedPosition == spinner.selectedItemPosition)
            return false
        if (selectedPosition < 0 || selectedPosition >= spinner.adapter.count)
            return false

        spinner.setSelection(selectedPosition)
        return true
    }

    private fun setSpinnerListener(spinner: Spinner, listener: InverseBindingListener) {
        if (spinner.onItemSelectedListener != null)
            return
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = listener.onChange()
            override fun onNothingSelected(adapterView: AdapterView<*>) = listener.onChange()
        }
    }
}