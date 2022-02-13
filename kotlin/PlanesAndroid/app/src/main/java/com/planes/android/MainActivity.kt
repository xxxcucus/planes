package com.planes.android

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.planes.android.about.AboutFragment
import com.planes.android.game.GameFragment
import com.planes.android.login.LoginFragment
import com.planes.android.preferences.*
import com.planes.android.videos.VideoFragment1
import com.planes.android.videos.VideoSettingsService
import com.planes.single_player_engine.GameStages
import com.planes.single_player_engine.PlanesRoundJava


class MainActivity : AppCompatActivity() {

    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var m_PlaneRound: PlanesRoundInterface
    private var m_SinglePlayerPreferencesService = SinglePlayerPreferencesServiceGlobal()
    private var m_MultiplayerPreferencesService = MultiplayerPreferencesServiceGlobal()
    private var m_MainPreferencesService = MainPreferencesServiceGlobal()
    private lateinit var m_VideoSettingsService: VideoSettingsService
    private var mSelectedItem = 0
    private lateinit var m_DrawerLayout: DrawerLayout
    private lateinit var m_ProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_TITLE

        m_ProgressBar = findViewById(R.id.ProgressBarBottom)
        m_ProgressBar.isIndeterminate = true

        m_PlaneRound = PlanesRoundJava()
        (m_PlaneRound as PlanesRoundJava).createPlanesRound()

        //TODO: create multiplayer round

        m_SinglePlayerPreferencesService.createPreferencesService(this)
        m_SinglePlayerPreferencesService.readPreferences()
        setPreferencesForPlaneRound()

        m_MultiplayerPreferencesService.createPreferencesService(this)
        m_MultiplayerPreferencesService.readPreferences()

        m_MainPreferencesService.createPreferencesService(this)
        m_MainPreferencesService.readPreferences()

        m_VideoSettingsService = VideoSettingsService(this)
        m_VideoSettingsService.readPreferences()

        m_DrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        mDrawerToggle = object : ActionBarDrawerToggle(this, m_DrawerLayout, R.string.drawer_open_content_description, R.string.drawer_closed_content_description) {
            override fun onDrawerClosed(view: View) {
                setFragment(true)
            }
        }
        m_DrawerLayout.addDrawerListener(mDrawerToggle)

        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            mSelectedItem = menuItem.itemId
            menuItem.setChecked(true)
            m_DrawerLayout.closeDrawer(navigationView)
            true
        }

        if (savedInstanceState != null) {
            m_SinglePlayerPreferencesService.readFromSavedInstanceState(savedInstanceState)
            m_MultiplayerPreferencesService.readFromSavedInstanceState(savedInstanceState)
            m_MainPreferencesService.readFromSavedInstanceState(savedInstanceState)
            m_VideoSettingsService.readFromSavedInstanceState(savedInstanceState)
            setPreferencesForPlaneRound()

            mSelectedItem = savedInstanceState.getInt("currentFragment")
        }

        if (m_MainPreferencesService.multiplayerVersion)
            setDraweMenuMultiplayer()
        else
            setDraweMenuSinglePlayer()

        if (mSelectedItem == 0) {
            mSelectedItem = R.id.nav_game
            setFragment(false)
        } else {
            setFragment(true)
        }

    }

    private fun setPreferencesForPlaneRound(): Boolean {
        var retVal = true

        if (m_PlaneRound.getComputerSkill() != m_SinglePlayerPreferencesService.computerSkill) {
            if (!m_PlaneRound.setComputerSkill(m_SinglePlayerPreferencesService.computerSkill)) {
                m_SinglePlayerPreferencesService.computerSkill = m_PlaneRound.getComputerSkill()
                retVal = false
            }
        }

        if (m_PlaneRound.getShowPlaneAfterKill() != m_SinglePlayerPreferencesService.showPlaneAfterKill) {
            if (!m_PlaneRound.setShowPlaneAfterKill(m_SinglePlayerPreferencesService.showPlaneAfterKill)) {
                m_SinglePlayerPreferencesService.showPlaneAfterKill = m_PlaneRound.getShowPlaneAfterKill()
                retVal = false
            }
        }

        if (!retVal) {
            onWarning(getString(R.string.warning_options_text))
        }

        return retVal
    }

    private fun setPreferencesForPlaneRound(computerSkill: Int, showPlaneAfterKill: Boolean): Boolean {
        var retVal = true

        if (m_PlaneRound.getComputerSkill() != computerSkill) {
            if (!m_PlaneRound.setComputerSkill(computerSkill)) {
                retVal = false
            }
        }

        if (m_PlaneRound.getShowPlaneAfterKill() != showPlaneAfterKill) {
            if (!m_PlaneRound.setShowPlaneAfterKill(showPlaneAfterKill)) {
                retVal = false
            }
        }

        if (!retVal) {
            onWarning(getString(R.string.warning_options_text))
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

        val id = item.itemId
        if (id == R.id.menu_help) {
            onButtonShowHelpWindowClick()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun setSinglePlayerOptions(currentSkill: Int, showPlaneAfterKill: Boolean): Boolean {
        if (!setPreferencesForPlaneRound(currentSkill, showPlaneAfterKill))
            return false
        m_SinglePlayerPreferencesService.computerSkill = currentSkill
        m_SinglePlayerPreferencesService.showPlaneAfterKill = showPlaneAfterKill
        return true
    }

    fun setMultiplayerOptions(username: String, password: String): Boolean {
        /*TODOif (!setPreferencesForPlaneRound(currentSkill, showPlaneAfterKill))
            return false*/
        m_MultiplayerPreferencesService.username = username
        m_MultiplayerPreferencesService.password = password
        return true
    }

    fun setDraweMenuMultiplayer() {
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView != null) {
            var menu = navigationView.menu
            menu.findItem(R.id.nav_login).setVisible(true)
            menu.findItem(R.id.nav_logout).setVisible(true)
            menu.findItem(R.id.nav_register).setVisible(true)
        }
    }

    fun setDraweMenuSinglePlayer() {
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView != null) {
            var menu = navigationView.menu
            menu.findItem(R.id.nav_login).setVisible(false)
            menu.findItem(R.id.nav_logout).setVisible(false)
            menu.findItem(R.id.nav_register).setVisible(false)
        }
    }

    fun setFragment(addToBackStack: Boolean) {
        lateinit var newFragment:Fragment

        when(mSelectedItem) {
            R.id.nav_settings -> {
                //TODO: check MainPreferencesService to see which Fragment to call
                if (!m_MainPreferencesService.multiplayerVersion)
                    newFragment = SinglePlayerSettingsFragment()
                else
                    newFragment = MultiplayerSettingsFragment()

            }
            R.id.nav_game -> {
                newFragment = GameFragment()
            }
            R.id.nav_videos -> {
                val bundle = Bundle()
                bundle.putInt("videosettings/currentVideo", m_VideoSettingsService.currentVideo)
                bundle.putSerializable("videosettings/videoPlaybackPositions", m_VideoSettingsService.videoPlaybackPositions)
                newFragment = VideoFragment1()
                newFragment.setArguments(bundle)
            }
            R.id.nav_about -> {
                newFragment = AboutFragment()
            }
            R.id.nav_login -> {
                newFragment = LoginFragment()
            }
        }

        if (mSelectedItem != 0) {

            if (addToBackStack)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, newFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack("FromMainMenu")
                    .commit()
            else
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, newFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
        }

        //mSelectedItem = 0
    }

    fun startTutorialFragment(index: Int) {
        mSelectedItem = R.id.nav_videos

        val bundle = Bundle()
        bundle.putInt("videosettings/currentVideo", index)
        bundle.putSerializable("videosettings/videoPlaybackPositions", m_VideoSettingsService.videoPlaybackPositions)
        var newFragment = VideoFragment1()
        newFragment.setArguments(bundle)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, newFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack("FromHelp")
            .commit()
    }

    /**
     * Enter multiplayer modus
     */
    fun restartPreferencesFragment() {
        if (m_MainPreferencesService.multiplayerVersion)
            setDraweMenuMultiplayer()
        else
            setDraweMenuSinglePlayer()

        supportFragmentManager.popBackStack("FromMainMenu", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        mSelectedItem = R.id.nav_settings
        setFragment(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        m_SinglePlayerPreferencesService.writeToSavedInstanceState(outState)
        m_MultiplayerPreferencesService.writeToSavedInstanceState(outState)
        m_MainPreferencesService.writeToSavedInstanceState(outState)
        m_VideoSettingsService.writeToSavedInstanceState(outState)
        outState.putInt("currentFragment", mSelectedItem)
        Log.d("Planes", "onSaveInstanceState")

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        m_SinglePlayerPreferencesService.writePreferences()
        m_MultiplayerPreferencesService.writePreferences()
        m_MainPreferencesService.writePreferences()
        m_VideoSettingsService.writePreferences()
        super.onStop()
        Log.d("Planes", "onStop")
    }

    public override fun onDestroy() {
        m_SinglePlayerPreferencesService.writePreferences()
        m_MultiplayerPreferencesService.writePreferences()
        m_MainPreferencesService.writePreferences()
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

    fun onWarning(errorString: String) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.warning_options, null)

        val textView = popupView.findViewById<TextView>(R.id.warning_options_text)
        textView.setText(errorString)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(
            findViewById<View>(R.id.coordinator_id) as LinearLayoutCompat,
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

    fun setActionBarTitle(title: String) {
        supportActionBar?.setTitle(title)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_planes, menu)
        return true
    }

    fun onButtonShowHelpWindowClick() {

        if (mSelectedItem in arrayOf(R.id.nav_about, R.id.nav_settings))
            return

        // inflate the layout of the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.help_popup, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(m_DrawerLayout, Gravity.CENTER, 0, 0)
        val helpTextView = popupView.findViewById(R.id.popup_help_text) as TextView
        val helpTitleTextView = popupView.findViewById(R.id.popup_help_title) as TextView
        val helpButton = popupView.findViewById(R.id.popup_help_button) as Button
        if (helpTextView != null && helpTitleTextView != null) {
            when(mSelectedItem) {
                R.id.nav_game -> {
                    showHelpGameFragment(popupWindow, helpTextView, helpTitleTextView, helpButton)
                }
                R.id.nav_videos -> {
                    showHelpVideoFragment(helpTextView, helpTitleTextView, helpButton)
                }
            }
        }

        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }

    fun showHelpGameFragment(popupWindow: PopupWindow, helpTextView: TextView, helpTitleTextView: TextView, helpButton: Button) {
        var gameStage = m_PlaneRound.getGameStage()
        when (gameStage) {
            GameStages.GameNotStarted.value -> {
                helpTitleTextView.text = resources.getString(R.string.game_not_started_stage)
                helpTextView.text = resources.getString(R.string.helptext_startnewgame_1)
                helpButton.setEnabled(false)
            }
            GameStages.BoardEditing.value -> {
                helpTitleTextView.text = resources.getString(R.string.board_editing_stage)
                helpTextView.text = """
                    ${resources.getString(R.string.helptext_boardediting_1)}
                    ${resources.getString(R.string.helptext_boardediting_2)}
                    """.trimIndent()
                helpButton.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View) {
                        startTutorialFragment(1)
                        popupWindow.dismiss()
                    }
                })
                helpButton.setEnabled(true)
            }
            GameStages.Game.value -> {
                helpTitleTextView.text = resources.getString(R.string.game_stage)
                helpTextView.text = """
                    ${resources.getString(R.string.helptext_game_1)}
                    ${resources.getString(R.string.helptext_game_2)}
                    """.trimIndent()
                helpButton.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View) {
                        startTutorialFragment(0)
                        popupWindow.dismiss()
                    }
                })
                helpButton.setEnabled(true)
            }
        }
    }

    fun showHelpVideoFragment(helpTextView: TextView, helpTitleTextView: TextView, helpButton: Button) {
        helpTitleTextView.text = resources.getString(R.string.videos)
        helpTextView.text = """
                    ${resources.getString(R.string.helptext_videos1)}
                    ${resources.getString(R.string.helptext_videos2)}
                    ${resources.getString(R.string.helptext_videos3)}
                    """.trimIndent()
        helpButton.setEnabled(false)
    }

    fun setCurrentFragmentId(curFragment: ApplicationScreens) {
        when(curFragment.value) {
            0 -> mSelectedItem = R.id.nav_settings
            1 -> mSelectedItem = R.id.nav_game
            2 -> mSelectedItem = R.id.nav_videos
            else -> mSelectedItem = R.id.nav_about
        }
    }

    fun startProgressDialog() {
        m_ProgressBar.isVisible = true
    }

    fun stopProgressDialog() {
        m_ProgressBar.isVisible = false
    }
}