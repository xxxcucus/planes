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
import com.planes.android.creategame.CreateGameFragment
import com.planes.android.creategame.CreateGameSettingsGlobal
import com.planes.android.game.multiplayer.GameFragmentMultiplayer
import com.planes.android.game.singleplayer.GameFragmentSinglePlayer
import com.planes.android.gamestats.GameStatsFragment
import com.planes.android.login.LoginFragment
import com.planes.android.register.RegisterFragment
import com.planes.android.preferences.*
import com.planes.android.register.NoRobotFragment
import com.planes.android.register.NoRobotSettingsService
import com.planes.android.videos.VideoFragment1
import com.planes.android.videos.VideoSettingsService
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.RegistrationResponse
import com.planes.single_player_engine.PlanesRoundJava


class MainActivity : AppCompatActivity() {

    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var m_PlaneRound: PlanesRoundInterface
    private lateinit var m_MultiplayerRound: MultiplayerRoundInterface
    private var m_SinglePlayerPreferencesService = SinglePlayerPreferencesServiceGlobal()
    private var m_MultiplayerPreferencesService = MultiplayerPreferencesServiceGlobal()
    private var m_MainPreferencesService = MainPreferencesServiceGlobal()
    private lateinit var m_VideoSettingsService: VideoSettingsService
    private lateinit var m_NoRobotSettingsService: NoRobotSettingsService
    private var m_CreateGameSettingsService = CreateGameSettingsGlobal()
    private var mSelectedItem = 0
    private lateinit var m_DrawerLayout: DrawerLayout
    private lateinit var m_ProgressBar: ProgressBar
    private lateinit var m_MainLayout: LinearLayoutCompat

    //region life cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_TITLE

        m_MainLayout = findViewById<View>(R.id.coordinator_id) as LinearLayoutCompat

        m_ProgressBar = findViewById(R.id.ProgressBarBottom)
        m_ProgressBar.isIndeterminate = true

        m_PlaneRound = PlanesRoundJava()
        (m_PlaneRound as PlanesRoundJava).createPlanesRound()

        m_MultiplayerRound = MultiplayerRoundJava()
        (m_MultiplayerRound as MultiplayerRoundJava).createPlanesRound()

        m_SinglePlayerPreferencesService.createPreferencesService(this)
        m_SinglePlayerPreferencesService.readPreferences()
        setPreferencesForPlaneRound()

        m_MultiplayerPreferencesService.createPreferencesService(this)
        m_MultiplayerPreferencesService.readPreferences()

        m_MainPreferencesService.createPreferencesService(this)
        m_MainPreferencesService.readPreferences()

        m_VideoSettingsService = VideoSettingsService(this)
        m_VideoSettingsService.readPreferences()

        m_CreateGameSettingsService.createPreferencesService(this)

        m_NoRobotSettingsService = NoRobotSettingsService()

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
            m_NoRobotSettingsService.readFromSavedInstanceState(savedInstanceState)
            m_CreateGameSettingsService.readFromSavedInstanceState(savedInstanceState)
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

    override fun onSaveInstanceState(outState: Bundle) {
        m_SinglePlayerPreferencesService.writeToSavedInstanceState(outState)
        m_MultiplayerPreferencesService.writeToSavedInstanceState(outState)
        m_MainPreferencesService.writeToSavedInstanceState(outState)
        m_VideoSettingsService.writeToSavedInstanceState(outState)
        m_NoRobotSettingsService.writeToSavedInstanceState(outState)
        m_CreateGameSettingsService.writeToSavedInstanceState(outState)
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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_planes, menu)
        return true
    }

    //endregion

    //region various

    fun setCurrentFragmentId(curFragment: ApplicationScreens) {
        when(curFragment.value) {
            ApplicationScreens.Preferences.value -> mSelectedItem = R.id.nav_settings
            ApplicationScreens.Game.value -> mSelectedItem = R.id.nav_game
            ApplicationScreens.Videos.value -> mSelectedItem = R.id.nav_videos
            ApplicationScreens.About.value -> mSelectedItem = R.id.nav_about
            ApplicationScreens.Login.value -> mSelectedItem = R.id.nav_login
            ApplicationScreens.Register.value -> mSelectedItem = R.id.nav_register
            ApplicationScreens.NoRobot.value -> mSelectedItem = R.id.nav_norobot
            ApplicationScreens.CreateGame.value -> mSelectedItem = R.id.nav_creategame
            ApplicationScreens.GameStats.value -> mSelectedItem = R.id.nav_game_status
            else -> mSelectedItem = R.id.nav_game
        }
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

    fun setActionBarTitle(title: String) {
        supportActionBar?.setTitle(title)
    }

    //endregion


    //region drawer
    fun setDraweMenuMultiplayer() {
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView != null) {
            var menu = navigationView.menu
            menu.findItem(R.id.nav_login).setVisible(true)
            menu.findItem(R.id.nav_logout).setVisible(true)
            menu.findItem(R.id.nav_register).setVisible(true)
        }

        setUsernameDrawerMenuMultiplayer()
    }

    fun setUsernameDrawerMenuMultiplayer() {
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        val header = navigationView.getHeaderView(0)
        var versionTextView = header.findViewById<TextView>(R.id.version_header)
        var userTextView = header.findViewById<TextView>(R.id.user_header)
        versionTextView.setText(getString(R.string.multiplayergame))
        userTextView.visibility = View.VISIBLE
        var username = m_MultiplayerRound.getUsername()
        if (username.isNullOrEmpty())
            userTextView.setText(getString(R.string.nouser))
        else
            userTextView.setText(username)
    }

    fun setDraweMenuSinglePlayer() {
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView != null) {
            var menu = navigationView.menu
            menu.findItem(R.id.nav_login).setVisible(false)
            menu.findItem(R.id.nav_logout).setVisible(false)
            menu.findItem(R.id.nav_register).setVisible(false)
            menu.findItem(R.id.nav_game_status).setVisible(false)
        }
        val header = navigationView.getHeaderView(0)
        var versionTextView = header.findViewById<TextView>(R.id.version_header)
        var userTextView = header.findViewById<TextView>(R.id.user_header)
        versionTextView.setText(getString(R.string.singleplayergame))
        userTextView.visibility = View.GONE
    }

    //endregion

    //region settings
    public fun setVideoSettings(currentVideo: Int, playbackPositions: IntArray) {
        m_VideoSettingsService.currentVideo = currentVideo
        m_VideoSettingsService.videoPlaybackPositions = playbackPositions
    }

    public fun setNorobotSettings(requestId: Long, images: Array<String>, question: String, selection: Array<Boolean>) {
        m_NoRobotSettingsService.requestId = requestId.toString()
        m_NoRobotSettingsService.question = question
        m_NoRobotSettingsService.images = images
        m_NoRobotSettingsService.selection = selection
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


    //endregion

    //region progress bar

    fun startProgressDialog() {
        m_ProgressBar.isVisible = true
    }

    fun stopProgressDialog() {
        m_ProgressBar.isVisible = false
    }

    //endregion

    //region start fragments
    fun setFragment(addToBackStack: Boolean) {
        lateinit var newFragment:Fragment

        when(mSelectedItem) {
            R.id.nav_settings -> {
                if (!m_MainPreferencesService.multiplayerVersion)
                    newFragment = SinglePlayerSettingsFragment()
                else
                    newFragment = MultiplayerSettingsFragment()

            }
            R.id.nav_game -> {
                if (!m_MainPreferencesService.multiplayerVersion)
                    newFragment = GameFragmentSinglePlayer()
                else
                    newFragment = GameFragmentMultiplayer()
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

            R.id.nav_register -> {
                newFragment = RegisterFragment()
            }

            R.id.nav_norobot -> {
                val bundle = Bundle()
                bundle.putString("norobot/requestid", m_NoRobotSettingsService.requestId)
                bundle.putSerializable("norobot/images", m_NoRobotSettingsService.images)
                bundle.putString("norobot/question", m_NoRobotSettingsService.question)
                bundle.putSerializable("norobot/selection", m_NoRobotSettingsService.selection)
                newFragment = NoRobotFragment()
                newFragment.setArguments(bundle)
            }

            R.id.nav_creategame -> {
                newFragment = CreateGameFragment()
            }

            R.id.nav_game_status -> {
                newFragment = GameStatsFragment()
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

    fun startNoRobotFragment(regResp : RegistrationResponse) {

        mSelectedItem = R.id.nav_norobot  //TODO theoretically not necessary because each fragment sets this variable when it starts

        var newFragment = NoRobotFragment()
        val bundle = Bundle()
        bundle.putString("norobot/requestid", regResp.m_Id)
        bundle.putString("norobot/question", regResp.m_Question)
        var images =  arrayOf(regResp.m_ImageId_1, regResp.m_ImageId_2, regResp.m_ImageId_3, regResp.m_ImageId_4, regResp.m_ImageId_5,
            regResp.m_ImageId_6, regResp.m_ImageId_7, regResp.m_ImageId_8, regResp.m_ImageId_9)
        bundle.putSerializable("norobot/images", images)
        var selection = arrayOf(false, false, false, false, false, false, false, false, false);
        bundle.putSerializable("norobot/selection", selection)

        newFragment.setArguments(bundle)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, newFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

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


    fun startRegistrationFragment(norobotError: Boolean, message: String) {
        mSelectedItem = R.id.nav_register
        setFragment(true)

        if (norobotError) {
            onWarning(message)
        } else {
            Tools.displayToast(getString(R.string.norobot_success), applicationContext)
        }
    }

    fun startGameFragment() {
        mSelectedItem = R.id.nav_game
        setFragment(true)
    }

    fun startLoginFragment() {
        mSelectedItem = R.id.nav_login
        setFragment(true)
    }

    fun startConnectToGameFragment() {
        mSelectedItem = R.id.nav_creategame
        setFragment(true)
    }

    fun startGameStatsFragment() {
        mSelectedItem = R.id.nav_game_status
        setFragment(true)
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

    //endregion

    //region popups
    fun onWarning(errorString: String) {
        Popups.onWarning(applicationContext, m_MainLayout, errorString)
    }

    fun showSaveCredentialsPopup(username: String, password: String) {
        if (m_MultiplayerPreferencesService.username == username && m_MultiplayerPreferencesService.password == password) {

            Tools.displayToast(getString(R.string.loginsuccess), applicationContext)

            return
        }

        Popups.showSaveCredentialsPopup(applicationContext, m_MainLayout, username, password,
            { username: String, password: String ->
                m_MultiplayerPreferencesService.username = username
                m_MultiplayerPreferencesService.password = password
            })
    }

    fun onButtonShowHelpWindowClick() {
        var helpPopup = HelpPopup(applicationContext, m_MainLayout, mSelectedItem, ::startTutorialFragment)
        helpPopup.onButtonShowHelpWindowClick()
    }

    //endregion
}