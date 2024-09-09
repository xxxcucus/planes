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
import com.planes.android.login.PlayersListServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.commobj.SimpleRequestNotConnectedToGameCommObj
import com.planes.multiplayer_engine.responses.LogoutResponse
import io.reactivex.Observable
import retrofit2.Response


class LogoutFragment: Fragment() {
    private lateinit var binding: FragmentLogoutBinding
    private lateinit var m_LogoutCommObj: SimpleRequestNotConnectedToGameCommObj<LogoutResponse>
    private var m_MultiplayerRound = MultiplayerRoundJava()
    private var m_PlayersListService = PlayersListServiceGlobal()
    private var m_CreateGameSettingsService = CreateGameSettingsGlobal()
    private lateinit var m_Context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_Context = context
        m_MultiplayerRound.createPlanesRound()
        m_CreateGameSettingsService.createPreferencesService()
        m_PlayersListService.createService()
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

    private fun createObservable() : Observable<Response<LogoutResponse>> {
        return m_MultiplayerRound.logout(m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId().toString())
    }
    public fun performLogout() {

        m_LogoutCommObj = SimpleRequestNotConnectedToGameCommObj(::createObservable,
        getString(R.string.error_logout), getString(R.string.unknownerror), getString(R.string.validation_user_not_loggedin),
         ::receiveLogoutStatus, ::finalizeLogoutSuccessful, requireActivity())
        
        m_LogoutCommObj.makeRequest()
    }

    fun receiveLogoutStatus(response: LogoutResponse): String {
        var errorString = ""
        if (!response.m_LoggedOut)
            errorString = getString(R.string.error_logout)
        return errorString
    }

    fun finalizeLogoutSuccessful() {
        m_MultiplayerRound.setUserData("", "", "")
        m_MultiplayerRound.resetGameData()
        m_MultiplayerRound.initRound()

        m_CreateGameSettingsService.createGameState = CreateGameStates.NotSubmitted
        m_CreateGameSettingsService.gameName = ""

        m_PlayersListService.stopPolling()

        if (!this::binding.isInitialized)
            return

        binding.logout.isEnabled = false
        binding.settingsData!!.m_LoginStatus = m_Context.resources.getString(R.string.nouser)
        binding.settingsData!!.m_Username = ""
        binding.invalidateAll()

        if (activity is MainActivity)
            (activity as MainActivity).setUsernameDrawerMenuMultiplayer()

    }

    override fun onDetach () {
        super.onDetach()
        hideLoading()
        if (this::m_LogoutCommObj.isInitialized)
            m_LogoutCommObj.disposeSubscription()
    }

    private fun hideLoading() {
        if (activity is MainActivity)
            (activity as MainActivity).stopProgressDialog()
    }

}