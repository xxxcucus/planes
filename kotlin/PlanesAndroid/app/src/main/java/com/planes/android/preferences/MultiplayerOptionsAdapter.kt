package com.planes.android.preferences

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.R

class MultiplayerOptionsAdapter(optionsList: List<BasisOptionsModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val m_SectionsList: List<BasisOptionsModel>

    init {
        this.m_SectionsList = optionsList
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0 || position == 1) //first two are username and password
            return 1
        return 0  //then switch to single player
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = if (viewType == 0)
            LayoutInflater.from(parent.context).inflate(R.layout.edittextoptions_layout, parent, false)
        else
            LayoutInflater.from(parent.context).inflate(R.layout.spinneroptions_layout, parent, false)

        return when (viewType) {
            0 -> return SpinnerOptionsView(itemView, parent.context)
            else -> return EditTextOptionsView(itemView, parent.context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section: BasisOptionsModel = m_SectionsList[position]

        when(holder.itemViewType) {
            0 -> {
                var spinnerModel = section as SpinnerOptionsModel
                var spinnerView = holder as SpinnerOptionsView
                spinnerView.m_Title.setText(spinnerModel.getTitle())
                spinnerView.m_Text.setText (spinnerModel.getText())
                spinnerView.setSpinnerOptions(spinnerModel.getSpinnerOptions())
            }
            else -> {
                var edittextModel = section as EditTextOptionsModel
                var edittextView = holder as EditTextOptionsView
                edittextView.m_Title.setText(edittextModel.getTitle())
                edittextView.m_Text.setText (edittextModel.getText())
                edittextView.m_EditText.setText(edittextModel.getEditableText())
            }
        }
    }

    override fun getItemCount(): Int {
        return m_SectionsList.size
    }
}