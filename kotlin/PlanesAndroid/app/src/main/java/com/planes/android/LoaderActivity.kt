package com.planes.android


import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.planes.android.creategame.CreateGameSettingsGlobal
import com.planes.android.creategame.CreateGameStates
import com.planes.android.preferences.MainPreferencesServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.VersionResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class LoaderActivity : AppCompatActivity() {

    private var m_MainPreferencesService = MainPreferencesServiceGlobal()
    private lateinit var m_VerifyVersionCommObj: Disposable
    private lateinit var m_ProgressBar: ProgressBar
    private lateinit var m_StaticProgressLabel: TextView
    var m_MultiplayerRound = MultiplayerRoundJava()
    var m_CreateGameSettingsService = CreateGameSettingsGlobal()
    private lateinit var m_MainLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)

        m_MainPreferencesService.createPreferencesService(this)
        m_MainPreferencesService.readPreferences()

        m_MultiplayerRound.createPlanesRound()
        m_CreateGameSettingsService.createPreferencesService()

        m_MainLayout = findViewById(R.id.loader_layout)

        m_ProgressBar = findViewById(R.id.ProgressBarBottom)
        m_ProgressBar.isIndeterminate = true

        m_StaticProgressLabel = findViewById(R.id.LoaderLabelBottom)

        var singlePlayerGameButton = findViewById(R.id.singleplayer) as Button
        var multiplayerGameButton = findViewById(R.id.multiplayer) as Button

        singlePlayerGameButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                m_MainPreferencesService.multiplayerVersion = false;
                m_MainPreferencesService.writePreferences()
                m_MultiplayerRound.setUserData("", "", "")
                m_MultiplayerRound.resetGameData()
                m_MultiplayerRound.initRound()
                m_CreateGameSettingsService.createGameState = CreateGameStates.NotSubmitted
                val intent = Intent(this@LoaderActivity, MainActivity::class.java)
                intent.putExtra("startScreen", ApplicationScreens.Game.value)
                startActivity(intent)
            }
        })



        multiplayerGameButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                m_VerifyVersionCommObj = m_MultiplayerRound.testServerVersion()
                    .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { startProgressDialog() }
                    .doOnTerminate { stopProgressDialog() }
                    .doOnComplete { stopProgressDialog() }
                    .subscribe({data -> checkServerVersion(data.body())}
                        , {error -> error.localizedMessage?.let { showError(it) } })

            }
        })
    }

    fun startProgressDialog() {
        if (!areSystemAnimationsEnabled())
            m_StaticProgressLabel.isVisible = true
        else
            m_ProgressBar.isVisible = true
    }

    fun stopProgressDialog() {
        m_StaticProgressLabel.isVisible = false
        m_ProgressBar.isVisible = false
    }

    private fun areSystemAnimationsEnabled(): Boolean {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        val powerSaveMode = powerManager.isPowerSaveMode
        return !powerSaveMode
    }

    fun checkServerVersion(body: VersionResponse?) {
        stopProgressDialog()

        if (body == null) {
            onWarning(getString(R.string.unknownerror));
        } else if (body.m_VersionString != m_MainPreferencesService.serverVersion) {
            val errorString = getString(R.string.server_version_error)
            onWarning(errorString)
        } else {
            m_MainPreferencesService.multiplayerVersion = true;
            m_MainPreferencesService.writePreferences()
            m_MultiplayerRound.setUserData("", "", "")
            m_MultiplayerRound.resetGameData()
            m_MultiplayerRound.initRound()
            m_CreateGameSettingsService.createGameState = CreateGameStates.NotSubmitted
            m_VerifyVersionCommObj.dispose()
            val intent = Intent(this@LoaderActivity, MainActivity::class.java)
            intent.putExtra("startScreen", ApplicationScreens.Login.value)
            startActivity(intent)
        }
    }

    fun showError(errorString: String) {
        stopProgressDialog()
        Popups.onWarning(this, m_MainLayout, errorString)
    }

    fun onWarning(errorString: String) {
        Popups.onWarning(this, m_MainLayout, errorString)
    }

    override fun onStop() {
        if (this::m_VerifyVersionCommObj.isInitialized)
            m_VerifyVersionCommObj.dispose()
        stopProgressDialog()
        super.onStop()
        Log.d("Loader", "onStop")
    }

}