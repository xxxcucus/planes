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
import com.planes.android.databinding.FragmentOptionsBinding
import com.planes.android.R
import com.planes.multiplayer_engine.MultiplayerRoundJava
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SinglePlayerSettingsFragment : Fragment() {
    private lateinit var binding: FragmentOptionsBinding
    private var m_InitialComputerSkill = 0
    private var m_InitialShowPlaneAfterKill = false
    private var m_InitialMultiplayerVersion = false
    private var m_PreferencesService = SinglePlayerPreferencesServiceGlobal()
    private var m_MainPreferencesService = MainPreferencesServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()

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
        binding = FragmentOptionsBinding.inflate(layoutInflater, container, false)
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
            var verifyVersion = m_MultiplayerRound.testServerVersion()
            verifyVersion
                .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
                .subscribeOn(Schedulers.io()) //run request in the background and deliver response to the main thread aka UI thread
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({_ -> showLoading()})
                .doOnTerminate({ hideLoading()})
            .subscribe({data ->
                Log.d("Planes","Version received: " + data.body().toString())
            }, {error ->
                Log.d("Planes","Connection error: " + error.toString())
            });
        }


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