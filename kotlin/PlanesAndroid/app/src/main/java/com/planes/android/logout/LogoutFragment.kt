package com.planes.android.logout

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R
import com.planes.android.creategame.CreateGameSettingsGlobal
import com.planes.android.creategame.CreateGameStates
import com.planes.android.databinding.FragmentLogoutBinding
import com.planes.multiplayer_engine.MultiplayerRoundJava


class LogoutFragment: Fragment() {
    public lateinit var binding: FragmentLogoutBinding
    public var m_MultiplayerRound = MultiplayerRoundJava()
    public var m_CreateGameSettingsService = CreateGameSettingsGlobal()
    public lateinit var m_Context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_Context = context
        m_MultiplayerRound.createPlanesRound()
        m_CreateGameSettingsService.createPreferencesService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogoutBinding.inflate(inflater, container, false)
        val username = m_MultiplayerRound.getUsername()

        binding.settingsData = LogoutViewModel(
            username,
            m_Context
        )

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.logout))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Logout)
            (activity as MainActivity).updateOptionsMenu()
        }

        val logoutButton = binding.logout

        logoutButton.setOnClickListener { performLogout() }
        if (username.isEmpty())
            logoutButton.isEnabled = false

        return binding.root
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        if (activity is MainActivity)
            return super.onGetLayoutInflater(savedInstanceState)

        val inflater = super.onGetLayoutInflater(savedInstanceState)
        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), R.style.MyAppTheme)
        return inflater.cloneInContext(contextThemeWrapper)
    }
    public fun performLogout() {
        m_MultiplayerRound.setUserData("", "", "")
        m_MultiplayerRound.resetGameData()
        m_MultiplayerRound.initRound()

        m_CreateGameSettingsService.createGameState = CreateGameStates.NotSubmitted
        m_CreateGameSettingsService.gameName = ""

        if (!this::binding.isInitialized)
            return

        binding.logout.isEnabled = false
        binding.settingsData!!.m_LoginStatus = m_Context.resources.getString(R.string.nouser)
        binding.settingsData!!.m_Username = ""
        binding.invalidateAll()

        if (activity is MainActivity)
            (activity as MainActivity).setUsernameDrawerMenuMultiplayer()
    }

}