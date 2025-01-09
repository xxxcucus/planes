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
    public var m_Version: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_Version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        prepareSectionsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_about)
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = m_AboutAdapter

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.about))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.About)
            (activity as MainActivity).updateOptionsMenu()
        }
    }

    private fun prepareSectionsList() {
        var version_section = AboutModel("Version", "Software version is unknown", false, "Empty", "Empty")

        if (m_Version != null)
        version_section = AboutModel(getString(R.string.software_version_title),
            getString(R.string.software_version) + " " + m_Version, false, "Empty", "Empty")
        val software_section = AboutModel(getString(R.string.credits_software_title),
            getString(R.string.credits_software_content), true, getString(R.string.credits_software_button),
            "https://www.github.com/xxxcucus/planes")
        val graphics_section = AboutModel(getString(R.string.credits_graphics_title),
            getString(R.string.credits_graphics_content1), true, getString(R.string.credits_graphics_button),
        "https://axa951.wixsite.com/portfolio")

        val otherContributionText = """
                ${getString(R.string.credits_othercontributions_1)}
                ${getString(R.string.credits_othercontributions_2)}
                ${getString(R.string.credits_othercontributions_3)}
                """.trimIndent()
        val others_section = AboutModel(getString(R.string.credits_othercontributions_title),
            otherContributionText, false, "Empty", "Empty")
        val website_section = AboutModel(getString(R.string.credits_website_title), getString(R.string.credits_website), true,
            getString(R.string.credits_website_button), "https://xxxcucus.github.io/planes/")
        val tools_section = AboutModel(getString(R.string.credits_tools_title),
            getString(R.string.credits_tools), false, "Empty", "Empty")

        m_SectionsList = arrayListOf(version_section, software_section, graphics_section, others_section, website_section, tools_section)
        m_AboutAdapter = AboutAdapter(m_SectionsList)
        m_AboutAdapter.notifyDataSetChanged()
    }
}