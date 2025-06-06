package com.planes.android.deleteuser

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
import com.planes.android.databinding.FragmentDeleteUserBinding
import com.planes.android.login.PlayersListServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.commobj.SimpleRequestNotConnectedToGameCommObj
import com.planes.multiplayer_engine.responses.DeleteUserResponse
import io.reactivex.Observable
import retrofit2.Response

class DeleteUserFragment: Fragment() {
    lateinit var binding: FragmentDeleteUserBinding
    lateinit var m_DeleteUserCommObj: SimpleRequestNotConnectedToGameCommObj<DeleteUserResponse>
    var m_MultiplayerRound = MultiplayerRoundJava()
    var m_PlayersListService = PlayersListServiceGlobal()
    var m_CreateGameSettingsService = CreateGameSettingsGlobal()
    lateinit var m_Context: Context

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
        binding = FragmentDeleteUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = m_MultiplayerRound.getUsername()

        binding.settingsData = DeleteUserViewModel(
            username,
            m_Context
        )
        val deleteUserButton = binding.deleteuser

        deleteUserButton.setOnClickListener { performDeleteUser() }
        if (username.isEmpty())
            deleteUserButton.isEnabled = false

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.delete_user))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.DeleteUser)
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

    private fun createObservable() : Observable<Response<DeleteUserResponse>> {
        return m_MultiplayerRound.deactivateUser(m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId().toString())
    }
    public fun performDeleteUser() {

        //TODO: update instrumented tests
        m_DeleteUserCommObj = SimpleRequestNotConnectedToGameCommObj(::createObservable,
            getString(R.string.error_deleteuser), getString(R.string.unknownerror), getString(R.string.validation_user_not_loggedin),
            ::receiveDeleteUserStatus, ::finalizeDeleteUserSuccessful, requireActivity())

        m_DeleteUserCommObj.makeRequest()
    }

    fun receiveDeleteUserStatus(response: DeleteUserResponse): String {
        var errorString = ""
        if (!response.m_Deactivated)
            errorString = getString(R.string.error_deleteuser)
        return errorString
    }

    fun finalizeDeleteUserSuccessful() {
        m_MultiplayerRound.setUserData("", "", "")
        m_MultiplayerRound.resetGameData()
        m_MultiplayerRound.initRound()

        m_CreateGameSettingsService.createGameState = CreateGameStates.NotSubmitted
        m_CreateGameSettingsService.gameName = ""

        m_PlayersListService.stopPolling()

        if (!this::binding.isInitialized)
            return

        binding.deleteuser.isEnabled = false
        binding.settingsData!!.m_LoginStatus = m_Context.resources.getString(R.string.nouser)
        binding.settingsData!!.m_Username = ""
        binding.invalidateAll()

        if (activity is MainActivity)
            (activity as MainActivity).setUsernameDrawerMenuMultiplayer()
    }

    override fun onDetach () {
        super.onDetach()
        hideLoading()
        if (this::m_DeleteUserCommObj.isInitialized)
            m_DeleteUserCommObj.disposeSubscription()
    }

    private fun hideLoading() {
        if (activity is MainActivity)
            (activity as MainActivity).stopProgressDialog()
    }

}