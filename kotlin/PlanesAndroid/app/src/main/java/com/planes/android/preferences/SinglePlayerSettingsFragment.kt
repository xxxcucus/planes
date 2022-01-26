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
    private var m_VersionError = false
    private var m_VersionErrorString = ""
    private lateinit var m_VerifyVersionSubscription: Disposable

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
        binding = FragmentOptionsSingleBinding.inflate(layoutInflater, container, false)
        m_InitialComputerSkill = m_PreferencesService.computerSkill
        m_InitialShowPlaneAfterKill = m_PreferencesService.showPlaneAfterKill
        m_InitialMultiplayerVersion = m_MainPreferencesService.multiplayerVersion
        binding.settingsData = SinglePlayerSettingsViewModel(m_InitialComputerSkill, m_InitialShowPlaneAfterKill, m_InitialMultiplayerVersion)
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
        if (this::m_VerifyVersionSubscription.isInitialized)
        m_VerifyVersionSubscription.dispose()
    }

    override fun onPause() {
        super.onPause()
    }

    fun showLoading() {
        (activity as MainActivity).startProgressDialog()
    }

    fun hideLoading() {
        (activity as MainActivity).stopProgressDialog()
    }

    fun checkServerVersion(versionString: String) {
        if (versionString != m_MainPreferencesService.serverVersion) {
            m_VersionError = true
            m_VersionErrorString = getString(R.string.server_version_error)
        }

        finalizeSaving()
    }

    fun setVersionError(errorMsg: String) {
        m_VersionError = true
        m_VersionErrorString = errorMsg
        finalizeSaving()
    }

    fun finalizeSaving() {
        if (!m_VersionError) {
            m_MainPreferencesService.multiplayerVersion = true
            (activity as MainActivity).restartPreferencesFragment()
        } else {
            (activity as MainActivity).onWarning(m_VersionErrorString)
            binding.settingsData!!.m_MultiplayerVersion = false
            binding.invalidateAll()
        }
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

        m_VersionError = false
        m_VersionErrorString = ""

        if (binding.settingsData!!.m_MultiplayerVersion) {
            var verifyVersion = m_MultiplayerRound.testServerVersion()
            m_VerifyVersionSubscription = verifyVersion
                .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> showLoading() }
                .doOnTerminate { hideLoading() }
                .doOnComplete { hideLoading() }
                .subscribe({data -> checkServerVersion(data.body()!!.versionString)}
                , {error -> setVersionError(error.localizedMessage.toString())});

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