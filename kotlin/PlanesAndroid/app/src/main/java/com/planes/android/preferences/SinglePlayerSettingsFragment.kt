package com.planes.android.preferences

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R
import com.planes.android.databinding.FragmentOptionsSingleBinding
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.commobj.SimpleRequestCommObj
import com.planes.multiplayer_engine.commobj.SimpleRequestWithoutCredentialsCommObj
import com.planes.multiplayer_engine.responses.StartNewRoundResponse
import com.planes.multiplayer_engine.responses.VersionResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.TimeUnit

class SinglePlayerSettingsFragment : Fragment() {
    private lateinit var binding: FragmentOptionsSingleBinding
    private var m_InitialComputerSkill = 0
    private var m_InitialShowPlaneAfterKill = false
    private var m_InitialMultiplayerVersion = false
    private var m_PreferencesService = SinglePlayerPreferencesServiceGlobal()
    private var m_MainPreferencesService = MainPreferencesServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()

    private lateinit var m_VerifyVersionCommObj: SimpleRequestWithoutCredentialsCommObj<VersionResponse>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_PreferencesService.createPreferencesService(context)
        m_MainPreferencesService.createPreferencesService(context)
        m_MultiplayerRound.createPlanesRound()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionsSingleBinding.inflate(inflater, container, false)
        m_InitialComputerSkill = m_PreferencesService.computerSkill
        m_InitialShowPlaneAfterKill = m_PreferencesService.showPlaneAfterKill
        m_InitialMultiplayerVersion = m_MainPreferencesService.multiplayerVersion
        binding.settingsData = SinglePlayerSettingsViewModel(m_InitialComputerSkill, m_InitialShowPlaneAfterKill, m_InitialMultiplayerVersion)
        (activity as MainActivity).setActionBarTitle(getString(R.string.options))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Preferences)
        var saveSettingsButton = binding.optionsSavesettings as Button
        saveSettingsButton.setOnClickListener(View.OnClickListener { writeToPreferencesService() })
        
        return binding.root
    }

    override fun onDetach () {
        super.onDetach()
        //TODO: move this in OnDestroy?
        if (this::m_VerifyVersionCommObj.isInitialized)
            m_VerifyVersionCommObj.disposeSubscription()
        hideLoading()
    }

    override fun onPause() {
        super.onPause()
    }

    fun hideLoading() {
        (activity as MainActivity).stopProgressDialog()
    }

    fun checkServerVersion(body: VersionResponse): String {
        var errorString = ""
        if (body.m_VersionString != m_MainPreferencesService.serverVersion) {
            errorString = getString(R.string.server_version_error)
        }

        return errorString
    }


    fun finalizeSavingSuccessful() {
        m_MainPreferencesService.multiplayerVersion = true
        (activity as MainActivity).restartPreferencesFragment()
    }

    fun finalizeSavingError() {
        binding.settingsData!!.m_MultiplayerVersion = false
        binding.invalidateAll()
    }

    fun createObservableVerifyVersion() : Observable<Response<VersionResponse>> {
        return m_MultiplayerRound.testServerVersion()
    }

    fun writeToPreferencesService() {

        /*
            if multiplayerVersion check connection to server, then set multiplayerVersion in MainPreferencesService

            m_MultiRound.getVersion - subscribe
            show wait animation until the request finishes

            if succesfull must
                mark changedVersion = true
                mark multiplayerVersion in m_MainPreferencesService

            then save the remaining options with the saveOptions function
            from the MainActivity

            if changedVersion load the Preferences fragment again from
            the MainActivity
        */
        if (!this::binding.isInitialized)
            return


        if (binding.settingsData!!.m_MultiplayerVersion) {
            m_VerifyVersionCommObj = SimpleRequestWithoutCredentialsCommObj<VersionResponse>( ::createObservableVerifyVersion,
                getString(R.string.version_error), getString(R.string.unknownerror), ::checkServerVersion, ::finalizeSavingSuccessful,
                ::finalizeSavingError, requireActivity())

            m_VerifyVersionCommObj.makeRequest()
        }

        if (!(activity as MainActivity).setSinglePlayerOptions(
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