package com.planes.android

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.planes.android.about.AboutFragment
import com.planes.android.chat.ChatFragment
import com.planes.android.chat.DatabaseServiceGlobal
import com.planes.android.chat.NewMessagesFlag
import com.planes.android.chat.NewMessagesServiceGlobal
import com.planes.android.conversation.ConversationFragment
import com.planes.android.creategame.CreateGameFragment
import com.planes.android.creategame.CreateGameSettingsGlobal
import com.planes.android.deleteuser.DeleteUserFragment
import com.planes.android.game.multiplayer.GameFragmentMultiplayer
import com.planes.android.game.singleplayer.GameFragmentSinglePlayer
import com.planes.android.gamestats.GameStatsFragment
import com.planes.android.login.LoginFragment
import com.planes.android.login.PlayersListServiceGlobal
import com.planes.android.login.ReceiveChatMessagesServiceGlobal
import com.planes.android.logout.LogoutFragment
import com.planes.android.preferences.MainPreferencesServiceGlobal
import com.planes.android.preferences.MultiplayerPreferencesServiceGlobal
import com.planes.android.preferences.MultiplayerSettingsFragment
import com.planes.android.preferences.SinglePlayerPreferencesServiceGlobal
import com.planes.android.preferences.SinglePlayerSettingsFragment
import com.planes.android.register.NoRobotFragment
import com.planes.android.register.NoRobotSettingsService
import com.planes.android.register.RegisterFragment
import com.planes.android.videos.VideoFragment1
import com.planes.android.videos.VideoSettingsService
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.RegistrationResponse
import com.planes.single_player_engine.GameStages
import com.planes.single_player_engine.PlanesRoundJava
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
    private var m_PlayersListService = PlayersListServiceGlobal()
    private var m_DatabaseService = DatabaseServiceGlobal()
    private var m_NewMessagesService = NewMessagesServiceGlobal()
    private var m_ReceiveChatMessagesService = ReceiveChatMessagesServiceGlobal()

    private var mSelectedItem = 0
    private lateinit var m_DrawerLayout: DrawerLayout
    private lateinit var m_ProgressBar: ProgressBar
    private lateinit var m_StaticProgressLabel: TextView
    private lateinit var m_MainLayout: LinearLayoutCompat
    private lateinit var m_NewMessagesMenuItem: MenuItem

    //region life cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        //enableEdgeToEdge();
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_TITLE

        m_MainLayout = findViewById<View>(R.id.coordinator_id) as LinearLayoutCompat

        m_ProgressBar = findViewById(R.id.ProgressBarBottom)
        m_ProgressBar.isIndeterminate = true

        m_StaticProgressLabel = findViewById(R.id.LoaderLabelBottom)

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

        m_PlayersListService.createService()
        m_DatabaseService.createService(this)
        m_NewMessagesService.createService()
        readNewMessagesFlagsFromDatabase()
        m_ReceiveChatMessagesService.createService(m_DatabaseService, m_NewMessagesService)
        m_ReceiveChatMessagesService.setMainActivityUpdateFunction { updateNewMessagesFlags() }

        m_DrawerLayout = findViewById(R.id.drawer_layout)
        mDrawerToggle = object : ActionBarDrawerToggle(this, m_DrawerLayout, R.string.drawer_open_content_description, R.string.drawer_closed_content_description) {
            override fun onDrawerClosed(view: View) {
                setFragment(true, "FromMainMenu")
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

        var fromIntent = false
        var bundle = intent.extras
        if (bundle != null) {
            val screenCode = bundle.getInt("startScreen")
            setCurrentFragmentId(ApplicationScreens[screenCode]!!)
            fromIntent = true
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

        if (m_MainPreferencesService.multiplayerVersion) {
            m_DatabaseService.deleteOldMessages(90)
        }

        val withHistory = mSelectedItem != 0 && !fromIntent

        if (mSelectedItem == 0) {
            mSelectedItem = R.id.nav_game
        }

        /*supportFragmentManager.addOnBackStackChangedListener {
            println("Backstack")
            for (i in 0 until supportFragmentManager.backStackEntryCount)
                println(supportFragmentManager.getBackStackEntryAt(i).name
            )
        }*/

        setFragment(withHistory, "AfterInitialization")

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
        } else if (id == R.id.menu_show_stats) {
            mSelectedItem = R.id.nav_game_status
            setFragment(true, "FromOptionsMenu")
        } else if (id == R.id.menu_cancel) {
            if (m_MultiplayerRound.getGameStage() == GameStages.Game.value) {
                val fragment: GameFragmentMultiplayer? =
                    supportFragmentManager.findFragmentByTag(ApplicationScreens.Game.toString()) as GameFragmentMultiplayer?
                fragment?.cancelRound()
            }
            if (m_PlaneRound.getGameStage() == GameStages.Game.value) {
                val fragment: GameFragmentSinglePlayer? =
                    supportFragmentManager.findFragmentByTag(ApplicationScreens.Game.toString()) as GameFragmentSinglePlayer?
                fragment?.cancelRound()
            }
        } else if (id == R.id.menu_newmessages) {
            mSelectedItem = R.id.nav_chat
            setFragment(true, "FromOptionsMenu")
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.menu_cancel).isVisible = isTablet() && (m_MultiplayerRound.getGameStage() == GameStages.Game.value ||
                m_PlaneRound.getGameStage() == GameStages.Game.value ) && mSelectedItem == R.id.nav_game
        menu.findItem(R.id.menu_show_stats).isVisible = isTablet() && (m_MultiplayerRound.getGameStage() == GameStages.Game.value)
                && mSelectedItem == R.id.nav_game
        m_NewMessagesMenuItem = menu.findItem(R.id.menu_newmessages)
        updateNewMessagesFlags()
        return true
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
        m_PlayersListService.stopPolling()
        m_ReceiveChatMessagesService.stopPolling()
        updateDatabaseFromNewMessagesFlags()
        super.onStop()
        Log.d("Planes", "onStop")
    }

    public override fun onDestroy() {
        m_SinglePlayerPreferencesService.writePreferences()
        m_MultiplayerPreferencesService.writePreferences()
        m_MainPreferencesService.writePreferences()
        m_VideoSettingsService.writePreferences()
        m_PlayersListService.stopPolling()
        m_ReceiveChatMessagesService.stopPolling()
        m_ReceiveChatMessagesService.deactivateUpdateOfMainActivity()
        updateDatabaseFromNewMessagesFlags()
        super.onDestroy()
        Log.d("Planes", "onDestroy")
    }

    public override fun onResume() {
        super.onResume()
        if (m_MainPreferencesService.multiplayerVersion && m_MultiplayerRound.isUserLoggedIn()) {
            m_PlayersListService.startPolling()
            m_ReceiveChatMessagesService.startPolling()
            m_ReceiveChatMessagesService.setMainActivityUpdateFunction { updateNewMessagesFlags() }
        }
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
            ApplicationScreens.DeleteUser.value -> R.id.nav_deleteuser
            ApplicationScreens.Chat.value -> R.id.nav_chat
            ApplicationScreens.Conversation.value ->R.id.nav_conversation
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
            menu.findItem(R.id.nav_deleteuser).isVisible = true
            menu.findItem(R.id.nav_chat).isVisible = false
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
        val menu = navigationView.menu
        val username = m_MultiplayerRound.getUsername()
        if (username.isEmpty()) {
            userTextView.text = getString(R.string.nouser)
            menu.findItem(R.id.nav_chat).isVisible = false
        } else {
            userTextView.text = username
            menu.findItem(R.id.nav_chat).isVisible = true
        }
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
            menu.findItem(R.id.nav_deleteuser).isVisible = false
            menu.findItem(R.id.nav_chat).isVisible = false
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
    //endregion

    //region start fragments
    fun setFragment(addToBackStack: Boolean, addToBackStackName: String) {
        lateinit var newFragment:Fragment
        var tag = "tag"

        when(mSelectedItem) {
            R.id.nav_settings -> {
                newFragment = if (!m_MainPreferencesService.multiplayerVersion)
                    SinglePlayerSettingsFragment()
                else
                    MultiplayerSettingsFragment()
                tag = ApplicationScreens.Preferences.toString()
            }
            R.id.nav_game -> {
                newFragment = if (!m_MainPreferencesService.multiplayerVersion)
                    GameFragmentSinglePlayer()
                else
                    GameFragmentMultiplayer()
                tag = ApplicationScreens.Game.toString()
            }
            R.id.nav_videos -> {
                val bundle = Bundle()
                bundle.putInt("videosettings/currentVideo", m_VideoSettingsService.currentVideo)
                bundle.putSerializable("videosettings/videoPlaybackPositions", m_VideoSettingsService.videoPlaybackPositions)
                newFragment = VideoFragment1()
                newFragment.setArguments(bundle)
                tag = ApplicationScreens.Videos.toString()
            }
            R.id.nav_about -> {
                newFragment = AboutFragment()
                tag = ApplicationScreens.About.toString()
            }
            R.id.nav_login -> {
                newFragment = LoginFragment()
                tag = ApplicationScreens.Login.toString()
            }
            R.id.nav_logout -> {
                newFragment = LogoutFragment()
                tag = ApplicationScreens.Logout.toString()
            }

            R.id.nav_register -> {
                newFragment = RegisterFragment()
                tag = ApplicationScreens.Register.toString()
            }

            R.id.nav_deleteuser -> {
                newFragment = DeleteUserFragment()
                tag = ApplicationScreens.DeleteUser.toString()
            }

            R.id.nav_norobot -> {
                val bundle = Bundle()
                bundle.putString("norobot/requestid", m_NoRobotSettingsService.requestId)
                bundle.putSerializable("norobot/images", m_NoRobotSettingsService.images)
                bundle.putString("norobot/question", m_NoRobotSettingsService.question)
                bundle.putSerializable("norobot/selection", m_NoRobotSettingsService.selection)
                newFragment = NoRobotFragment()
                newFragment.setArguments(bundle)
                tag = ApplicationScreens.NoRobot.toString()
            }

            R.id.nav_creategame -> {
                newFragment = CreateGameFragment()
                tag = ApplicationScreens.CreateGame.toString()
            }

            R.id.nav_game_status -> {
                newFragment = GameStatsFragment()
                tag = ApplicationScreens.GameStats.toString()
            }

            R.id.nav_chat -> {
                newFragment = ChatFragment()
                tag = ApplicationScreens.Chat.toString()
            }
        }

        if (mSelectedItem != 0) {

            if (addToBackStack)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, newFragment, tag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(addToBackStackName)
                    .commit()
            else
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, newFragment, tag)
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
            .replace(R.id.main_content, newFragment, ApplicationScreens.NoRobot.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack("FromRegister")
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
            .replace(R.id.main_content, newFragment, ApplicationScreens.Videos.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack("FromHelp")
            .commit()
    }


    fun startRegistrationFragment(norobotError: Boolean, message: String) {
        mSelectedItem = R.id.nav_register
        setFragment(true, "FromNoRobot")

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
                startLoginFragment("FromNoRobot")
            }
        })
        snackbar1.show()
    }

    fun startRegistrationFragment() {
        mSelectedItem = R.id.nav_register
        setFragment(true, "FromLogin")
    }

    fun startGameFragment() {
        mSelectedItem = R.id.nav_game
        setFragment(true, "FromCreateGame")
    }

    fun startLoginFragment(nameForBackStack: String) {
        mSelectedItem = R.id.nav_login
        setFragment(true, nameForBackStack)
    }

    fun startConnectToGameFragment(nameForBackStack: String) {
        mSelectedItem = R.id.nav_creategame
        setFragment(true, nameForBackStack)
    }

    fun startGameStatsFragment() {
        mSelectedItem = R.id.nav_game_status
        setFragment(true, "FromGame")
    }

    fun startChatFragment() {
        mSelectedItem = R.id.nav_chat
        setFragment(true, "FromLogin")
    }

    fun startConversationFragment(userid : Long, username : String) {
        mSelectedItem = R.id.nav_conversation

        val bundle = Bundle()
        bundle.putLong("conversation/userid", userid)
        bundle.putString("conversation/username", username)
        val newFragment = ConversationFragment()
        newFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, newFragment, ApplicationScreens.Conversation.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack("FromUsersList")
            .commit()
    }

    /**
     * Enter multiplayer modus
     */
    fun switchSingleMultiplayerVersion() {
        if (m_MainPreferencesService.multiplayerVersion) {
            Tools.displayToast(getString(R.string.multiplayergame), this)
            setDrawerMenuMultiplayer()
        } else {
            Tools.displayToast(getString(R.string.singleplayergame), this)
            setDrawerMenuSinglePlayer()
        }

        supportFragmentManager.popBackStack("FromMainMenu", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        mSelectedItem = if (m_MainPreferencesService.multiplayerVersion) {
            R.id.nav_login
        } else {
            R.id.nav_game
        }
        setFragment(true, "SwitchingPlayModus")
    }

    //endregion

    //region popups
    fun onWarning(errorString: String) {
        Popups.onWarning(this, m_MainLayout, errorString)
    }

    fun showSaveCredentialsPopup(username: String, password: String) {
        if (m_MultiplayerPreferencesService.username == username && m_MultiplayerPreferencesService.password == password) {
            Tools.displayToast(getString(R.string.loginsuccess), this)
            //startGameFragment()
            return
        }

        Popups.showSaveCredentialsPopup(this, m_MainLayout, username, password
        ) { username_: String, password_: String ->
            m_MultiplayerPreferencesService.username = username_
            m_MultiplayerPreferencesService.password = password_
        }
        Tools.displayToast(getString(R.string.loginsuccess), this)
        //startGameFragment()
    }

    private fun onButtonShowHelpWindowClick(multiplayerVersion: Boolean) {
        val helpPopup = HelpPopup(this, m_MainLayout, mSelectedItem, ::startTutorialFragment)
        helpPopup.onButtonShowHelpWindowClick(multiplayerVersion)
    }
    //endregion

    fun updateOptionsMenu() {
        invalidateOptionsMenu()
    }

    //region chat
    fun updateNewMessagesFlags() {
        if (m_MultiplayerRound.isUserLoggedIn()) {
            m_NewMessagesMenuItem.isVisible = m_NewMessagesService.areNewMessagesForPlayer(m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId())
        } else {
            m_NewMessagesMenuItem.isVisible = false
        }
    }

    fun readNewMessagesFlagsFromDatabase() {
        var newMessages: List<NewMessagesFlag> = emptyList()

        lifecycleScope.launch(Dispatchers.IO) {
            newMessages = m_DatabaseService.getNewMessagesFlags()
            withContext(Dispatchers.Main) {
                m_NewMessagesService.resetFlags(newMessages)
            }
        }
    }

    fun updateDatabaseFromNewMessagesFlags() {
        var newMessagesStatus = m_NewMessagesService.getNewMessagesFlags()

        for ((messageIdent, newMessages) in newMessagesStatus) {
            m_DatabaseService.updateNewMessagesFlags(
                messageIdent.senderName,
                messageIdent.senderId,
                messageIdent.receiverName,
                messageIdent.receiverId,
                newMessages
            )
        }
    }


    //endregion chat
}
