package com.planes.android.preferences

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.*
import com.planes.android.about.AboutAdapter
import com.planes.android.about.AboutModel
import com.planes.android.databinding.FragmentOptionsBinding

class SinglePlayerSettingsFragment : Fragment() {
    private lateinit var binding: FragmentOptionsBinding
    private var m_InitialComputerSkill = 0
    private var m_InitialShowPlaneAfterKill = false
    private var m_InitialMultiplayerVersion = false
    private var m_PreferencesService = SinglePlayerPreferencesServiceGlobal()
    private var m_MainPreferencesService = MainPreferencesServiceGlobal()

    private lateinit var m_SectonsList: List<BasisOptionsModel>
    private lateinit var m_PreferencesAdapter: SinglePlayerOptionsAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_PreferencesService.createPreferencesService(context)
        m_MainPreferencesService.createPreferencesService(context)
        prepareSectionsList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_options1, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recycler_options)

        var mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = m_PreferencesAdapter

        m_InitialComputerSkill = m_PreferencesService.computerSkill
        m_InitialShowPlaneAfterKill = m_PreferencesService.showPlaneAfterKill
        m_InitialMultiplayerVersion = m_MainPreferencesService.multiplayerVersion

        (activity as MainActivity).setActionBarTitle(getString(R.string.options))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Preferences)
        var saveSettingsButton = binding.root.findViewById(R.id.options_savesettings) as Button
        if (saveSettingsButton != null) {
            saveSettingsButton.setOnClickListener(View.OnClickListener { writeToPreferencesService() })
        }

        return rootView
    }

    fun prepareSectionsList() {
        var section_computerskill = SpinnerOptionsModel(getString(R.string.computer_skill))

        m_SectonsList = arrayListOf(version_section, software_section, graphics_section, others_section, tools_section)
        m_PreferencesAdapter = SinglePlayerOptionsAdapter(m_SectonsList)
        m_PreferencesAdapter.notifyDataSetChanged()
    }

    override fun onDetach () {
        super.onDetach()
    }

    override fun onPause() {
        super.onPause()
    }

    fun writeToPreferencesService() {
        //TODO: check if during game, check if multiplayer version has changed
        m_PreferencesService.writePreferences()
        m_MainPreferencesService.writePreferences()
    }
}