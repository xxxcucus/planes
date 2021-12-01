package com.planes_multiplayer.android

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.planes_multiplayer.single_player_engine.PlanesRoundJava


class MainActivity : AppCompatActivity() {

    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var m_PlaneRound: PlanesRoundInterface
    private lateinit var m_PreferencesService: PreferencesService
    private lateinit var m_VideoSettingsService: VideoSettingsService
    private var mSelectedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_TITLE


        m_PlaneRound = PlanesRoundJava()
        (m_PlaneRound as PlanesRoundJava).createPlanesRound()

        m_PreferencesService = PreferencesService(this)
        m_PreferencesService.readPreferences()
        setPreferencesForPlaneRound(true)

        m_VideoSettingsService = VideoSettingsService(this)
        m_VideoSettingsService.readPreferences()

        var drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        mDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open_content_description, R.string.drawer_closed_content_description) {
            override fun onDrawerClosed(view: View) {
                setFragment(true)
            }
        }
        drawerLayout.addDrawerListener(mDrawerToggle)

        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            mSelectedItem = menuItem.itemId
            menuItem.setChecked(true)
            drawerLayout.closeDrawer(navigationView)
            true
        }

        if (savedInstanceState != null) {
            m_PreferencesService.readFromSavedInstanceState(savedInstanceState)
            m_VideoSettingsService.readFromSavedInstanceState(savedInstanceState)
            setPreferencesForPlaneRound(true)

            mSelectedItem = savedInstanceState.getInt("currentFragment")
        }

        if (mSelectedItem == 0) {
            mSelectedItem = R.id.nav_game
            setFragment(false)
        } else {
            setFragment(true)
        }

    }

    private fun setPreferencesForPlaneRound(initial: Boolean): Boolean {
        var retVal = true

        if (m_PlaneRound.getComputerSkill() != m_PreferencesService.computerSkill) {
            if (!m_PlaneRound.setComputerSkill(m_PreferencesService.computerSkill)) {
                if (initial)
                    m_PreferencesService.computerSkill = m_PlaneRound.getComputerSkill()
                retVal = false
            }
        }

        if (m_PlaneRound.getShowPlaneAfterKill() != m_PreferencesService.showPlaneAfterKill) {
            if (!m_PlaneRound.setShowPlaneAfterKill(m_PreferencesService.showPlaneAfterKill)) {
                if (initial)
                    m_PreferencesService.showPlaneAfterKill = m_PlaneRound.getShowPlaneAfterKill()
                retVal = false
            }
        }

        if (!retVal) {
            onWarning();
        }

        return retVal
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun setOptions(currentSkill: Int, showPlaneAfterKill: Boolean): Boolean {
        m_PreferencesService.computerSkill = currentSkill
        m_PreferencesService.showPlaneAfterKill = showPlaneAfterKill

        return setPreferencesForPlaneRound(false)
    }

    fun setFragment(addToBackStack: Boolean) {
        lateinit var newFragment:Fragment

        when(mSelectedItem) {
            R.id.nav_settings -> {
                val bundle = Bundle()
                bundle.putInt("gamedifficulty/computerskill", m_PreferencesService.computerSkill)
                bundle.putBoolean("gamedifficulty/showkilledplane", m_PreferencesService.showPlaneAfterKill)
                newFragment = SettingsFragment()
                newFragment.setArguments(bundle)
                supportActionBar?.setTitle("Preferences")
            }
            R.id.nav_game -> {
                newFragment = GameFragment()
                supportActionBar?.setTitle("Game")
            }
            R.id.nav_videos -> {
                val bundle = Bundle()
                bundle.putInt("videosettings/currentVideo", m_VideoSettingsService.currentVideo)
                bundle.putSerializable("videosettings/videoPlaybackPositions", m_VideoSettingsService.videoPlaybackPositions)
                newFragment = VideoFragment1()
                newFragment.setArguments(bundle)
                supportActionBar?.setTitle("Videos")
            }
            R.id.nav_about -> {
                newFragment = AboutFragment()
                supportActionBar?.setTitle("About")
            }
        }

        if (mSelectedItem != 0) {

            if (addToBackStack)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, newFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack("FromMainMenu")
                    .commit();
            else
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, newFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }

        //mSelectedItem = 0
    }

    override fun onSaveInstanceState(outState: Bundle) {
        m_PreferencesService.writeToSavedInstanceState(outState)
        m_VideoSettingsService.writeToSavedInstanceState(outState)
        outState.putInt("currentFragment", mSelectedItem)
        Log.d("Planes", "onSaveInstanceState")

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        m_PreferencesService.writePreferences()
        m_VideoSettingsService.writePreferences()
        super.onStop()
        Log.d("Planes", "onStop")
    }

    public override fun onDestroy() {
        m_PreferencesService.writePreferences()
        m_VideoSettingsService.writePreferences()
        super.onDestroy()
        Log.d("Planes", "onDestroy")
    }

    public fun isHorizontal(): Boolean {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            true
        } else {
            false
        }
    }

    public fun isTablet(): Boolean {
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        return if (tabletSize) {
            true
        } else {
           false
        }
    }

    public fun setVideoSettings(currentVideo: Int, playbackPositions: IntArray) {
        m_VideoSettingsService.currentVideo = currentVideo
        m_VideoSettingsService.videoPlaybackPositions = playbackPositions
    }

    fun onWarning() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.warning_options, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(
            findViewById<View>(R.id.options_layout) as LinearLayout,
            Gravity.CENTER,
            0,
            0
        )

        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }


}