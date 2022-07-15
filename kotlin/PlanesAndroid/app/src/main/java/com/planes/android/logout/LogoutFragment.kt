package com.planes.android.logout

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R
import com.planes.android.databinding.FragmentLogoutBinding
import com.planes.android.databinding.FragmentStatusBinding
import com.planes.android.gamestats.GameStatsViewModel
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.single_player_engine.GameStages


class LogoutFragment: Fragment() {
    private lateinit var binding: FragmentLogoutBinding
    private var m_MultiplayerRound = MultiplayerRoundJava()
    private lateinit var m_Context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_Context = context
        m_MultiplayerRound.createPlanesRound()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutBinding.inflate(inflater, container, false)
        var username = m_MultiplayerRound.getUsername()

        binding.settingsData = LogoutViewModel(
            username,
            m_Context
        )
        (activity as MainActivity).setActionBarTitle(getString(R.string.logout))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Logout)

        var logoutButton = binding.logout as Button

        logoutButton.setOnClickListener(View.OnClickListener { performLogout() })
        if (username.isNullOrEmpty())
            logoutButton.isEnabled = false

        return binding.root
    }

    fun performLogout() {
        m_MultiplayerRound.setUserData("", "", "")
        m_MultiplayerRound.resetGameData()
        m_MultiplayerRound.initRound()

        if (!this::binding.isInitialized)
            return

        binding.logout.isEnabled = false
        binding.settingsData!!.m_LoginStatus = m_Context.resources.getString(R.string.nouser)
        binding.settingsData!!.m_Username = ""
        binding.invalidateAll()

        (activity as MainActivity).setUsernameDrawerMenuMultiplayer()
    }

}