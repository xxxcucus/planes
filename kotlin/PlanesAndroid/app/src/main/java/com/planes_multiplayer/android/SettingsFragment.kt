package com.planes_multiplayer.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.fragment.app.Fragment
import com.planes_multiplayer.android.databinding.FragmentOptionsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentOptionsBinding
    private var m_InitialComputerSkill = 0
    private var m_InitialShowPlaneAfterKill = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionsBinding.inflate(layoutInflater, container, false)
        m_InitialComputerSkill = requireArguments().getInt("gamedifficulty/computerskill")
        m_InitialShowPlaneAfterKill = requireArguments().getBoolean("gamedifficulty/showkilledplane")
        binding.settingsData = SettingsViewModel(m_InitialComputerSkill, m_InitialShowPlaneAfterKill)

        return binding.root
    }

    override fun onDetach () {
        writeToPreferencesService()
        super.onDetach()
    }

    override fun onPause() {
        super.onPause()
        writeToPreferencesService()
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
            }
        }
    }
}