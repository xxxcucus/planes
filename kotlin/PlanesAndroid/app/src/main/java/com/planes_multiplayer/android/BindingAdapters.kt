package com.planes_multiplayer.android

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

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
        val adapter_showPlane = ArrayAdapter.createFromResource(
            spinner.context,
            R.array.yesno_options, R.layout.spinner_item
        )
        spinner.adapter = adapter_showPlane
        setCurrentSelection(spinner, if (showPlaneAfterKill)  1 else 0)
        setSpinnerListener(spinner, listener)
    }

    @InverseBindingAdapter(attribute = "showPlaneAfterKill")
    @JvmStatic fun getShowPlaneAfterKill(spinner: Spinner): Boolean {
        return if (spinner.selectedItemPosition == 0) false else true
    }

    @JvmStatic private fun setCurrentSelection(spinner: Spinner, selectedPosition: Int): Boolean {
        if (selectedPosition == spinner.selectedItemPosition)
            return false
        if (selectedPosition < 0 || selectedPosition >= spinner.adapter.count)
            return false

        spinner.setSelection(selectedPosition)
        return true
    }

    @JvmStatic private fun setSpinnerListener(spinner: Spinner, listener: InverseBindingListener) {
        if (spinner.onItemSelectedListener != null)
            return
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = listener.onChange()
            override fun onNothingSelected(adapterView: AdapterView<*>) = listener.onChange()
        }
    }
}