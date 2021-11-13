package com.planes_multiplayer.android

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
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
        // recovering the instance state
        m_PreferencesService.readPreferences()
        if (!(m_PlaneRound as PlanesRoundJava).setComputerSkill(m_PreferencesService.computerSkill)) {
            m_PreferencesService.computerSkill = (m_PlaneRound as PlanesRoundJava).getComputerSkill()
        }
        if (!(m_PlaneRound as PlanesRoundJava).setShowPlaneAfterKill(m_PreferencesService.showPlaneAfterKill)) {
            m_PreferencesService.showPlaneAfterKill = (m_PlaneRound as PlanesRoundJava).getShowPlaneAfterKill()
        }


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
            m_PreferencesService.computerSkill =
                savedInstanceState.getInt("gamedifficulty/computerskill")
            m_PreferencesService.showPlaneAfterKill =
                savedInstanceState.getBoolean("gamedifficulty/showkilledplane")
            if (!m_PlaneRound.setComputerSkill(m_PreferencesService.computerSkill)) {
                m_PreferencesService.computerSkill = m_PlaneRound.getComputerSkill()
            }
            if (!m_PlaneRound.setShowPlaneAfterKill(m_PreferencesService.showPlaneAfterKill)) {
                m_PreferencesService.showPlaneAfterKill = m_PlaneRound.getShowPlaneAfterKill()
            }

            mSelectedItem = savedInstanceState.getInt("currentFragment")
        }

        if (mSelectedItem == 0) {
            mSelectedItem = R.id.nav_game
            setFragment(false)
        } else {
            setFragment(true)
        }

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

    fun setOptions(currentSkill: Int, showPlaneAfterKill: Boolean) {
        m_PreferencesService.computerSkill = currentSkill
        m_PreferencesService.showPlaneAfterKill = showPlaneAfterKill
    }

    fun playVideo(position: Int, title: String) {
        var newFragment = ExoplayerFragment()

        val bundle = Bundle()
        bundle.putInt("position", position)
        newFragment.setArguments(bundle)
        supportActionBar?.setTitle(title)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, newFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack("PlayVideoAtPosition")
            .commit();
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
                newFragment = VideoFragment()
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
        outState.putInt("gamedifficulty/computerskill", m_PreferencesService.computerSkill)
        outState.putBoolean("gamedifficulty/showkilledplane", m_PreferencesService.showPlaneAfterKill)
        outState.putInt("currentFragment", mSelectedItem)
        Log.d("Planes", "onSaveInstanceState")

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        m_PreferencesService.writePreferences()
        super.onStop()
        Log.d("Planes", "onStop")
    }

    public override fun onDestroy() {
        m_PreferencesService.writePreferences()
        super.onDestroy()
        Log.d("Planes", "onDestroy")
    }

}