package com.planes.android


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import com.planes.android.preferences.MainPreferencesServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.commobj.SimpleRequestWithoutCredentialsCommObj
import com.planes.multiplayer_engine.responses.VersionResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class LoaderActivity : AppCompatActivity() {

    private var m_MainPreferencesService = MainPreferencesServiceGlobal()
    private lateinit var m_VerifyVersionCommObj: Disposable
    private lateinit var m_ProgressBar: ProgressBar
    var m_MultiplayerRound = MultiplayerRoundJava()
    private lateinit var m_MainLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)

        m_MainPreferencesService.createPreferencesService(this)
        m_MainPreferencesService.readPreferences()

        m_MultiplayerRound.createPlanesRound()

        m_MainLayout = findViewById(R.id.loader_layout)

        m_ProgressBar = findViewById(R.id.ProgressBarBottom)
        m_ProgressBar.isIndeterminate = true

        var singlePlayerGameButton = findViewById(R.id.singleplayer) as Button
        var multiplayerGameButton = findViewById(R.id.multiplayer) as Button

        singlePlayerGameButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                m_MainPreferencesService.multiplayerVersion = false;
                m_MainPreferencesService.writePreferences()
                m_MultiplayerRound.setUserData("", "", "")
                m_MultiplayerRound.resetGameData()
                m_MultiplayerRound.initRound()
                val intent = Intent(this@LoaderActivity, MainActivity::class.java)
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
        m_ProgressBar.isVisible = true
    }

    fun stopProgressDialog() {
       m_ProgressBar.isVisible = false
    }

    fun checkServerVersion(body: VersionResponse?) {
        stopProgressDialog()

        if (body == null) {
            onWarning(getString(R.string.unknownerror));
        } else if (body!!.m_VersionString != m_MainPreferencesService.serverVersion) {
            val errorString = getString(R.string.server_version_error)
            onWarning(errorString)
        } else {
            m_MainPreferencesService.multiplayerVersion = true;
            m_MainPreferencesService.writePreferences()
            m_MultiplayerRound.setUserData("", "", "")
            m_MultiplayerRound.resetGameData()
            m_MultiplayerRound.initRound()
            m_VerifyVersionCommObj.dispose()
            val intent = Intent(this@LoaderActivity, MainActivity::class.java)
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