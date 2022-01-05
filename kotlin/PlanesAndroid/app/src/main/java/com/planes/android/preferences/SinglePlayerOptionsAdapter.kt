package com.planes.android.preferences

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.R

class SinglePlayerOptionsAdapter(optionsSectionsList: List<SpinnerOptionsModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val m_SectionsList: List<SpinnerOptionsModel>


    init {
        this.m_SectionsList = optionsSectionsList
    }

    /*override fun getItemViewType(position: Int): Int {
        return 0
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.spinneroptions_layout, parent, false)
        return SpinnerOptionsView(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section: SpinnerOptionsModel = m_SectionsList[position]

        var spinnerView = holder as SpinnerOptionsView
        spinnerView.m_Title.setText(section.getTitle())
        spinnerView.m_Text.setText (section.getText())
        spinnerView.setSpinnerOptions(section.getSpinnerOptions())

    }

    override fun getItemCount(): Int {
        return m_SectionsList.size
    }
}