package com.planes.android

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.annotation.ColorInt
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
import com.google.android.material.snackbar.Snackbar
import com.planes.android.about.AboutFragment
import com.planes.android.creategame.CreateGameFragment
import com.planes.android.creategame.CreateGameSettingsGlobal
import com.planes.android.game.multiplayer.GameFragmentMultiplayer
import com.planes.android.game.singleplayer.GameFragmentSinglePlayer
import com.planes.android.gamestats.GameStatsFragment
import com.planes.android.login.LoginFragment
import com.planes.android.logout.LogoutFragment
import com.planes.android.preferences.*
import com.planes.android.register.NoRobotFragment
import com.planes.android.register.NoRobotSettingsService
import com.planes.android.register.RegisterFragment
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

        val toolbar: Toolbar = findViewById(R.id.toolbar)
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

        m_CreateGameSettingsService.createPreferencesService()

        m_NoRobotSettingsService = NoRobotSettingsService()

        m_DrawerLayout = findViewById(R.id.drawer_layout)
        mDrawerToggle = object : ActionBarDrawerToggle(this, m_DrawerLayout, R.string.drawer_open_content_description, R.string.drawer_closed_content_description) {
            override fun onDrawerClosed(view: View) {
                setFragment(true)
            }
        }
        m_DrawerLayout.addDrawerListener(mDrawerToggle)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            mSelectedItem = menuItem.itemId
            menuItem.isChecked = true
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
            setDrawerMenuMultiplayer()
        else
            setDrawerMenuSinglePlayer()

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
            onButtonShowHelpWindowClick(m_MainPreferencesService.multiplayerVersion)
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

        val typedValue = TypedValue()
        val theme: Resources.Theme = getTheme()
        theme.resolveAttribute(R.attr.planesToolbarForegroundColor, typedValue, true)
        @ColorInt val toolbarForegroundColor: Int = typedValue.data

        for (i in 0 until menu.size()) {
            val drawable = menu.getItem(i).icon
            if (drawable != null) {
                drawable.mutate()
                drawable.setColorFilter(
                    toolbarForegroundColor,
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }
        return true
    }

    //endregion

    //region various

    fun setCurrentFragmentId(curFragment: ApplicationScreens) {
        mSelectedItem = when(curFragment.value) {
            ApplicationScreens.Preferences.value -> R.id.nav_settings
            ApplicationScreens.Game.value -> R.id.nav_game
            ApplicationScreens.Videos.value -> R.id.nav_videos
            ApplicationScreens.About.value -> R.id.nav_about
            ApplicationScreens.Login.value -> R.id.nav_login
            ApplicationScreens.Logout.value -> R.id.nav_logout
            ApplicationScreens.Register.value -> R.id.nav_register
            ApplicationScreens.NoRobot.value -> R.id.nav_norobot
            ApplicationScreens.CreateGame.value -> R.id.nav_creategame
            ApplicationScreens.GameStats.value -> R.id.nav_game_status
            else -> R.id.nav_game
        }
    }

    fun isHorizontal(): Boolean {
        val orientation = resources.configuration.orientation
        return orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    //endregion


    //region drawer
    private fun setDrawerMenuMultiplayer() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView != null) {
            val menu = navigationView.menu
            menu.findItem(R.id.nav_login).isVisible = true
            menu.findItem(R.id.nav_logout).isVisible = true
            menu.findItem(R.id.nav_register).isVisible = true
            menu.findItem(R.id.nav_game_status).isVisible = true
            menu.findItem(R.id.nav_creategame).isVisible = true
        }

        setUsernameDrawerMenuMultiplayer()
    }

    fun setUsernameDrawerMenuMultiplayer() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val header = navigationView.getHeaderView(0)
        val versionTextView = header.findViewById<TextView>(R.id.version_header)
        val userTextView = header.findViewById<TextView>(R.id.user_header)
        versionTextView.text = getString(R.string.multiplayergame)
        userTextView.visibility = View.VISIBLE
        val username = m_MultiplayerRound.getUsername()
        if (username.isEmpty())
            userTextView.text = getString(R.string.nouser)
        else
            userTextView.text = username
    }

    private fun setDrawerMenuSinglePlayer() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView != null) {
            val menu = navigationView.menu
            menu.findItem(R.id.nav_login).isVisible = false
            menu.findItem(R.id.nav_logout).isVisible = false
            menu.findItem(R.id.nav_register).isVisible = false
            menu.findItem(R.id.nav_game_status).isVisible = false
            menu.findItem(R.id.nav_creategame).isVisible = false
        }
        val header = navigationView.getHeaderView(0)
        val versionTextView = header.findViewById<TextView>(R.id.version_header)
        val userTextView = header.findViewById<TextView>(R.id.user_header)
        versionTextView.text = getString(R.string.singleplayergame)
        userTextView.visibility = View.GONE
    }

    //endregion

    //region settings
    fun setVideoSettings(currentVideo: Int, playbackPositions: IntArray) {
        m_VideoSettingsService.currentVideo = currentVideo
        m_VideoSettingsService.videoPlaybackPositions = playbackPositions
    }

    fun setNorobotSettings(requestId: Long, images: Array<String>, question: String, selection: Array<Boolean>) {
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
                newFragment = if (!m_MainPreferencesService.multiplayerVersion)
                    SinglePlayerSettingsFragment()
                else
                    MultiplayerSettingsFragment()

            }
            R.id.nav_game -> {
                newFragment = if (!m_MainPreferencesService.multiplayerVersion)
                    GameFragmentSinglePlayer()
                else
                    GameFragmentMultiplayer()
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
            R.id.nav_logout -> {
                newFragment = LogoutFragment()
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

        val newFragment = NoRobotFragment()
        val bundle = Bundle()
        bundle.putString("norobot/requestid", regResp.m_Id)
        bundle.putString("norobot/question", regResp.m_Question)
        val images =  arrayOf(regResp.m_ImageId_1, regResp.m_ImageId_2, regResp.m_ImageId_3, regResp.m_ImageId_4, regResp.m_ImageId_5,
            regResp.m_ImageId_6, regResp.m_ImageId_7, regResp.m_ImageId_8, regResp.m_ImageId_9)
        bundle.putSerializable("norobot/images", images)
        val selection = arrayOf(false, false, false, false, false, false, false, false, false)
        bundle.putSerializable("norobot/selection", selection)

        newFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, newFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

    }


    private fun startTutorialFragment(index: Int) {
        mSelectedItem = R.id.nav_videos

        val bundle = Bundle()
        bundle.putInt("videosettings/currentVideo", index)
        bundle.putSerializable("videosettings/videoPlaybackPositions", m_VideoSettingsService.videoPlaybackPositions)
        val newFragment = VideoFragment1()
        newFragment.arguments = bundle

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
            showRegistrationSuccessfullSnack()
        }
    }

    fun showRegistrationSuccessfullSnack() {
        val snackbar1 =
            Snackbar.make(m_MainLayout, getString(R.string.norobot_success), Snackbar.LENGTH_LONG)
        snackbar1.setAction(getString(R.string.login).uppercase(), object : View.OnClickListener {
            override fun onClick(view: View?) {
                startLoginFragment()
            }
        })
        snackbar1.show()
    }

    fun startRegistrationFragment() {
        mSelectedItem = R.id.nav_register
        setFragment(true)
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
    fun switchSingleMultiplayerVersion() {
        if (m_MainPreferencesService.multiplayerVersion) {
            Tools.displayToast(getString(R.string.multiplayergame), applicationContext)
            setDrawerMenuMultiplayer()
        } else {
            Tools.displayToast(getString(R.string.singleplayergame), applicationContext)
            setDrawerMenuSinglePlayer()
        }

        supportFragmentManager.popBackStack("FromMainMenu", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        mSelectedItem = if (m_MainPreferencesService.multiplayerVersion) {
            R.id.nav_login
        } else {
            R.id.nav_game
        }
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
            //startGameFragment()
            return
        }

        Popups.showSaveCredentialsPopup(applicationContext, m_MainLayout, username, password
        ) { username_: String, password_: String ->
            m_MultiplayerPreferencesService.username = username_
            m_MultiplayerPreferencesService.password = password_
        }
        Tools.displayToast(getString(R.string.loginsuccess), applicationContext)
        //startGameFragment()
    }

    private fun onButtonShowHelpWindowClick(multiplayerVersion: Boolean) {
        val helpPopup = HelpPopup(applicationContext, m_MainLayout, mSelectedItem, ::startTutorialFragment)
        helpPopup.onButtonShowHelpWindowClick(multiplayerVersion)
    }
    //endregion
}
