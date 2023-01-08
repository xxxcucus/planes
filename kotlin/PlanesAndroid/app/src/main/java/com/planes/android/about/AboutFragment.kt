package com.planes.android.about

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R

class AboutFragment : Fragment() {

    private lateinit var m_SectionsList: List<AboutModel>
    private lateinit var m_AboutAdapter: AboutAdapter
    public lateinit var m_Version: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_Version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        prepareSectionsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_about1, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recycler_about)

        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = m_AboutAdapter

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.about))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.About)
        }
        return rootView
    }

    private fun prepareSectionsList() {
        val version_section = AboutModel(getString(R.string.software_version_title),
            getString(R.string.software_version) + " " + m_Version, false, "Empty", "Empty")
        val software_section = AboutModel(getString(R.string.credits_software_title),
            getString(R.string.credits_software_content), true, getString(R.string.credits_software_button),
            "https://www.github.com/xxxcucus/planes")
        val graphics_section = AboutModel(getString(R.string.credits_graphics_title),
            getString(R.string.credits_graphics_content), true, getString(R.string.credits_graphics_button),
        "https://axa951.wixsite.com/portfolio")
        val others_section = AboutModel(getString(R.string.credits_othercontributions_title),
            getString(R.string.credits_othercontributions), false, "Empty", "Empty")
        val tools_section = AboutModel(getString(R.string.credits_tools_title),
            getString(R.string.credits_tools), false, "Empty", "Empty")

        m_SectionsList = arrayListOf(version_section, software_section, graphics_section, others_section, tools_section)
        m_AboutAdapter = AboutAdapter(m_SectionsList)
        m_AboutAdapter.notifyDataSetChanged()
    }
}