package com.planes.android.about
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.R
import com.planes.android.Tools


class AboutAdapter(aboutSectionsList: List<AboutModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val m_SectionsList: List<AboutModel>


    init {
        this.m_SectionsList = aboutSectionsList
    }

    inner class MyViewHolderWithButton(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        var m_Title: TextView
        var m_Text: TextView
        var m_Button: Button
        var m_ButtonLink: String = ""
        var m_Context: Context

        init {
            m_Title = view.findViewById(R.id.about_section_title)
            m_Text = view.findViewById(R.id.about_section_text)
            m_Button = view.findViewById(R.id.about_section_button)
            m_Context = context

            m_Button.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    Tools.openLink(m_Context, m_ButtonLink)
                }
            })
        }

        fun setButtonLink(link: String) {
            m_ButtonLink = link
        }
    }

    inner class MyViewHolderWithoutButton(view: View) : RecyclerView.ViewHolder(view) {
        var m_Title: TextView
        var m_Text: TextView

        init {
            m_Title = view.findViewById(R.id.about_section_title)
            m_Text = view.findViewById(R.id.about_section_text)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 1 || position == 2)
            return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = if (viewType == 0)
            LayoutInflater.from(parent.context).inflate(R.layout.aboutlayout, parent, false)
        else
            LayoutInflater.from(parent.context).inflate(R.layout.aboutlayout1, parent, false)

        return when (viewType) {
            0 -> return MyViewHolderWithButton(itemView, parent.context)
            else -> return MyViewHolderWithoutButton(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section: AboutModel = m_SectionsList[position]

        when(holder.itemViewType) {
            0 -> {
                var holderwithButton = holder as MyViewHolderWithButton
                holderwithButton.m_Title.setText(section.getTitle())
                holderwithButton.m_Text. setText (section.getText())
                holderwithButton.m_Button.setText(section.getTextButton())
                holderwithButton.setButtonLink(section.getLinkButton())
            }
            else -> {
                var holderwithoutButton = holder as MyViewHolderWithoutButton
                holderwithoutButton.m_Title.setText(section.getTitle())
                holderwithoutButton.m_Text.setText (section.getText())
            }
        }
    }

    override fun getItemCount(): Int {
        return m_SectionsList.size
    }
}