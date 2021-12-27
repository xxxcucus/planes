package com.planes.android

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.planes.android.databinding.FragmentOptionsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentOptionsBinding
    private var m_InitialComputerSkill = 0
    private var m_InitialShowPlaneAfterKill = false
    private var m_PreferencesService = PreferencesServiceGlobal()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_PreferencesService.createPreferencesService(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionsBinding.inflate(layoutInflater, container, false)
        m_InitialComputerSkill = m_PreferencesService.computerSkill
        m_InitialShowPlaneAfterKill = m_PreferencesService.showPlaneAfterKill
        binding.settingsData = SettingsViewModel(m_InitialComputerSkill, m_InitialShowPlaneAfterKill)
        (activity as MainActivity).setActionBarTitle(getString(R.string.options))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Preferences)
        var saveSettingsButton = binding.root.findViewById(R.id.options_savesettings) as Button
        if (saveSettingsButton != null) {
            saveSettingsButton.setOnClickListener(View.OnClickListener { writeToPreferencesService() })
        }

        return binding.root
    }

    override fun onDetach () {
        super.onDetach()
    }

    override fun onPause() {
        super.onPause()
    }

    fun writeToPreferencesService() {
        if (this::binding.isInitialized) {
            if (!(activity as MainActivity).setOptions(
                    binding.settingsData!!.m_ComputerSkill,
                    binding.settingsData!!.m_ShowPlaneAfterKill
                )
            ) {
                binding.settingsData!!.m_ComputerSkill = m_InitialComputerSkill
                binding.settingsData!!.m_ShowPlaneAfterKill = m_InitialShowPlaneAfterKill
                binding.invalidateAll()
            }
        }
    }
}