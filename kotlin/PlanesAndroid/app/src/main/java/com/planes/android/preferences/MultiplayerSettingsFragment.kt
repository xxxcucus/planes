package com.planes.android.preferences

import android.content.Context
import android.os.Bundle
import android.text.InputType.*
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
import com.planes.android.databinding.FragmentOptionsMultiBinding
import com.planes.android.login.PlayersListServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava


//TODO to update according to google and udemy
class MultiplayerSettingsFragment : Fragment() {
    private lateinit var binding: FragmentOptionsMultiBinding
    private var m_InitialUsername = ""
    private var m_InitialPassword = ""
    private var m_InitialMultiplayerVersion = true
    private var m_PreferencesService = MultiplayerPreferencesServiceGlobal()
    private var m_MainPreferencesService = MainPreferencesServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()
    var m_CreateGameSettingsService = CreateGameSettingsGlobal()
    private var m_PlayersListService = PlayersListServiceGlobal()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_PreferencesService.createPreferencesService(context)
        m_MainPreferencesService.createPreferencesService(context)
        m_CreateGameSettingsService.createPreferencesService()
        m_MultiplayerRound.createPlanesRound()
        m_PlayersListService.createService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOptionsMultiBinding.inflate(inflater, container, false)  //TODO first parameter maybe inflater
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        m_InitialUsername = m_PreferencesService.username
        m_InitialPassword = m_PreferencesService.password
        m_InitialMultiplayerVersion = m_MainPreferencesService.multiplayerVersion
        binding.settingsData = MultiplayerSettingsViewModel(m_InitialUsername, m_InitialPassword, m_InitialMultiplayerVersion)


        val saveSettingsButton = binding.optionsSavesettings
        saveSettingsButton.setOnClickListener { writeToPreferencesService() }

        val hidePasswordCheckbox = binding.secureCheck
        hidePasswordCheckbox.setOnCheckedChangeListener { _, isChecked ->
            hideShowPassword(
                isChecked
            )
        }
        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.options))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Preferences)
            (activity as MainActivity).updateOptionsMenu()
        }
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        if (activity is MainActivity)
            return super.onGetLayoutInflater(savedInstanceState)

        val inflater = super.onGetLayoutInflater(savedInstanceState)
        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), R.style.MyAppTheme)
        return inflater.cloneInContext(contextThemeWrapper)
    }
    private fun writeToPreferencesService() {

        if (!this::binding.isInitialized)
            return


        if (!(activity as MainActivity).setMultiplayerOptions(
                binding.settingsData!!.m_Username,
                binding.settingsData!!.m_Password
            )
        ) {
            binding.settingsData!!.m_Username = m_InitialUsername
            binding.settingsData!!.m_Password = m_InitialPassword
            binding.invalidateAll()
        }

        if (!binding.settingsData!!.m_MultiplayerVersion) {
            m_MainPreferencesService.multiplayerVersion = false
            m_MultiplayerRound.setUserData("", "", "")
            m_MultiplayerRound.resetGameData()
            m_MultiplayerRound.initRound()
            m_PlayersListService.stopPolling()
            m_CreateGameSettingsService.createGameState = CreateGameStates.NotSubmitted
            (activity as MainActivity).switchSingleMultiplayerVersion()
        }

    }

    private fun hideShowPassword(isChecked: Boolean) {
        if (isChecked) {
            binding.passwordEdittext.inputType =
                TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
        } else {
            binding.passwordEdittext.inputType =
                TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        binding.invalidateAll()
    }
}