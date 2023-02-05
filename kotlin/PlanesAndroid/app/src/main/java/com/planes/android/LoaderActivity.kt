package com.planes.android


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.planes.android.preferences.MainPreferencesServiceGlobal

class LoaderActivity : AppCompatActivity() {

    private var m_MainPreferencesService = MainPreferencesServiceGlobal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)

        m_MainPreferencesService.createPreferencesService(this)
        m_MainPreferencesService.readPreferences()

        var singlePlayerGameButton = findViewById(R.id.singleplayer) as Button
        var multiplayerGameButton = findViewById(R.id.multiplayer) as Button

        singlePlayerGameButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                m_MainPreferencesService.multiplayerVersion = false;
                m_MainPreferencesService.writePreferences()
                val intent = Intent(this@LoaderActivity, MainActivity::class.java)
                startActivity(intent)
            }
        })

        multiplayerGameButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                m_MainPreferencesService.multiplayerVersion = true;
                m_MainPreferencesService.writePreferences()
                val intent = Intent(this@LoaderActivity, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }
}