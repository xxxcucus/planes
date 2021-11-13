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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionsBinding.inflate(layoutInflater, container, false)
        val computerSkill = requireArguments().getInt("gamedifficulty/computerskill")
        val showPlaneAfterKill = requireArguments().getBoolean("gamedifficulty/showkilledplane")
        binding.settingsData = SettingsViewModel(computerSkill, showPlaneAfterKill)

        return binding.root
    }

    override fun onDetach () {
        (activity as MainActivity).setOptions(binding.settingsData!!.m_ComputerSkill, binding.settingsData!!.m_ShowPlaneAfterKill)
        super.onDetach()
    }
}