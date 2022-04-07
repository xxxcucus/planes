package com.planes.android.creategame

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.planes.android.*
import com.planes.android.databinding.FragmentCreateGameBinding
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.ConnectToGameResponse
import com.planes.multiplayer_engine.responses.CreateGameResponse
import com.planes.multiplayer_engine.responses.ErrorResponse
import com.planes.multiplayer_engine.responses.GameStatusResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import java.util.concurrent.TimeUnit

//TODO to update accordint to google and udemy
class CreateGameFragment: Fragment() {
    private lateinit var binding: FragmentCreateGameBinding
    private var m_GameName = ""
    private var m_CreateGameError = false
    private var m_CreateGameErrorString = ""
    private var m_MultiplayerRound = MultiplayerRoundJava()
    private lateinit var m_CreateGameSubscription: Disposable
    private lateinit var m_ConnectToGameSubscription: Disposable
    private lateinit var m_RefreshGameStatusSubscription: Disposable
    private var m_CreateGameSettingsService = CreateGameSettingsGlobal()
    private lateinit var m_Context: Context
    private lateinit var m_MainLayout: RelativeLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_MultiplayerRound.createPlanesRound()
        m_CreateGameSettingsService.createPreferencesService(context)
        m_Context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateGameBinding.inflate(inflater, container, false)
        binding.settingsData = CreateGameViewModel(m_CreateGameSettingsService.gameName)
        (activity as MainActivity).setActionBarTitle(getString(R.string.create_connectto_game))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.CreateGame)

        var createGameButton = binding.creategame as Button
        createGameButton.setOnClickListener(View.OnClickListener {
            m_MultiplayerRound.resetGameData()
            setCreateGameSettings(CreateGameStates.Submitted)
            checkGameStatus()
        })

        var generateGameNameButton = binding.generateGamename
        generateGameNameButton.setOnClickListener {
            binding.settingsData!!.m_GameName = generateRandonGameName()
            setCreateGameSettings(CreateGameStates.NotSubmitted)
            binding.ProgressBarCreateGame.isVisible = false
            binding.startPlaying.isEnabled = false
            binding.invalidateAll()
        }

        var gameNameEdit = binding.gamenameEdittext as EditText

        gameNameEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setCreateGameSettings(CreateGameStates.NotSubmitted)
                binding.ProgressBarCreateGame.isVisible = false
                binding.startPlaying.isEnabled = false
            }
        })

        m_MainLayout = binding.rootCreategame as RelativeLayout

        //TODO: load animation visible or not depending on state

        when(m_CreateGameSettingsService.createGameState) {
            CreateGameStates.NotSubmitted -> {
                binding.ProgressBarCreateGame.isVisible = false
                binding.startPlaying.isEnabled = false
            }
            CreateGameStates.Submitted -> {
                binding.ProgressBarCreateGame.isVisible = false
                binding.startPlaying.isEnabled = false
                checkGameStatus()
            }
            CreateGameStates.GameCreated -> { pollForGameConnection() }
            CreateGameStates.ConnectedToGame -> { connectedToGame() }
        }

        return binding.root
    }

    override fun onDetach () {
        super.onDetach()
        hideLoading()
        if (this::m_RefreshGameStatusSubscription.isInitialized)
            m_RefreshGameStatusSubscription.dispose()
        if (this::m_CreateGameSubscription.isInitialized)
            m_CreateGameSubscription.dispose()
        if (this::m_ConnectToGameSubscription.isInitialized)
            m_ConnectToGameSubscription.dispose()
    }

    override fun onPause() {
        super.onPause()
    }

    fun reactToGameStatus(code: Int, jsonErrorString: String?, headrs: Headers, body: GameStatusResponse?) {
        if (body != null)  {
           if (!body!!.m_Exists) {
               showCreateGamePopup()
           } else if (body!!.m_FirstPlayerName == body!!.m_SecondPlayerName) {
               showConnectToGamePopup()
           } else {
                m_CreateGameErrorString = getString(R.string.gamename_impossible)
                m_CreateGameError = true
           }
        } else {
            m_CreateGameErrorString = Tools.parseJsonError(jsonErrorString, getString(R.string.creategame_error),
                getString(R.string.unknownerror))
            m_CreateGameError = true
        }
        finalizeCreateGame()
    }

    /*
    if (!exists) {
        QMessageBox msgBox(this);
        msgBox.setText("You may create a new game with this name");
        QPushButton* createButton = msgBox.addButton("Create New Game", QMessageBox::YesRole);
        QPushButton* cancelButton = msgBox.addButton("Cancel", QMessageBox::NoRole);
        msgBox.exec();
        if (msgBox.clickedButton() == createButton)
            createGameSlot();
    } else if (firstPlayerName == secondPlayerName) {
        QMessageBox msgBox(this);
        msgBox.setText("You may connect to the game created by " + firstPlayerName);
        QPushButton* connectButton = msgBox.addButton("Connect to Game", QMessageBox::YesRole);
        QPushButton* cancelButton = msgBox.addButton("Cancel", QMessageBox::NoRole);
        msgBox.exec();
        if (msgBox.clickedButton() == connectButton)
            connectToGameSlot();
    } else {
        QMessageBox msgBox(this);
        msgBox.setText("It is not possible to create or connect to a a game with this name");
        msgBox.exec();
    }

     */

    fun setCreateGameError(errorMsg: String) {
        m_CreateGameError = true
        m_CreateGameErrorString = errorMsg
        finalizeCreateGame()
    }

    fun finalizeCreateGame() {
        if (m_CreateGameError) {
            (activity as MainActivity).onWarning(m_CreateGameErrorString)
        } else {
            //(activity as MainActivity).setUsernameDrawerMenuMultiplayer()
        }
    }

    fun checkGameStatus() {

        if (!this::binding.isInitialized)
            return

        m_CreateGameError = false
        m_CreateGameErrorString = ""

        if (!userLoggedIn()) {
            finalizeCreateGame()
            return
        }

        if (!validationGameName(binding.settingsData!!.m_GameName.trim())) {
            finalizeCreateGame()
            return
        }

        var gameStatus = m_MultiplayerRound.refreshGameStatus(binding.settingsData!!.m_GameName.trim())
        m_RefreshGameStatusSubscription = gameStatus
            .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> showLoading() }
            .doOnTerminate { hideLoading() }
            .doOnComplete { hideLoading() }
            .subscribe({data -> reactToGameStatus(data.code(), data.errorBody()?.string(), data.headers(), data.body())}
                , {error -> setCreateGameError(error.localizedMessage.toString())});
    }

    fun showLoading() {
        (activity as MainActivity).startProgressDialog()
    }

    fun hideLoading() {
        (activity as MainActivity).stopProgressDialog()
    }

    fun validationGameName(gameName: String) : Boolean {
        var retString = ""

        if (gameName.length > 30) {
            retString += " " + getString(R.string.validation_toolong_gamename)
        }

        if (gameName.length < 5) {
            retString += " " + getString(R.string.validation_tooshort_gamename)
        }

        if (!retString.isNullOrEmpty()) {
            m_CreateGameError = true
            m_CreateGameErrorString = retString
            return false
        }

        return true
    }

    fun generateRandonGameName(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val STRING_LENGTH = 10;

        val randomString = (1..STRING_LENGTH)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

        return randomString
    }

    fun userLoggedIn(): Boolean {
        if (!m_MultiplayerRound.isUserLoggedIn()) {
            m_CreateGameError = true
            m_CreateGameErrorString = getString(R.string.validation_user_not_loggedin)
            return false
        }
        return true
    }

    fun showCreateGamePopup() {
        Popups.showCreateNewGamePopup(m_Context, m_MainLayout, ::createGame);
    }

    fun showConnectToGamePopup() {
        Popups.showConnectToGamePopup(m_Context, m_MainLayout, ::connectToGame);
    }

    fun createGame() {
        var createGame = m_MultiplayerRound.createGame(binding.settingsData!!.m_GameName.trim())
        m_CreateGameSubscription = createGame
            .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> showLoading() }
            .doOnTerminate { hideLoading() }
            .doOnComplete { hideLoading() }
            .subscribe({data -> reactToGameCreation(data.code(), data.errorBody()?.string(), data.headers(), data.body())}
                , {error -> setCreateGameError(error.localizedMessage.toString())});
    }

    fun reactToGameCreation(code: Int, jsonErrorString: String?, headrs: Headers, body: CreateGameResponse?) {
        if (body != null)  {
            m_MultiplayerRound.setGameData(body!!)
            m_MultiplayerRound.setUserId(body!!.m_SecondPlayerId.toLong())
            setCreateGameSettings(CreateGameStates.GameCreated)
            pollForGameConnection()
        } else {
            m_CreateGameErrorString = Tools.parseJsonError(jsonErrorString, getString(R.string.creategame_error),
                getString(R.string.unknownerror))
            m_CreateGameError = true
            finalizeCreateGame()
        }
    }

    fun connectToGame() {
        var connectToGame = m_MultiplayerRound.connectToGame(binding.settingsData!!.m_GameName.trim())
        m_ConnectToGameSubscription = connectToGame
            .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> showLoading() }
            .doOnTerminate { hideLoading() }
            .doOnComplete { hideLoading() }
            .subscribe({data -> reactToConnectToGame(data.code(), data.errorBody()?.string(), data.headers(), data.body())}
                , {error -> setCreateGameError(error.localizedMessage.toString())});
    }

    fun reactToConnectToGame(code: Int, jsonErrorString: String?, headrs: Headers, body: ConnectToGameResponse?) {
        if (body != null)  {
            m_MultiplayerRound.setGameData(body!!)
            m_MultiplayerRound.setUserId(body!!.m_SecondPlayerId.toLong())
            setCreateGameSettings(CreateGameStates.ConnectedToGame)
            connectedToGame()
        } else {
            m_CreateGameErrorString = Tools.parseJsonError(jsonErrorString, getString(R.string.creategame_error),
                getString(R.string.unknownerror))
            m_CreateGameError = true
            finalizeCreateGame()
        }
    }

    fun setCreateGameSettings(state: CreateGameStates) {
        m_CreateGameSettingsService.createGameState = state
        m_CreateGameSettingsService.gameName = binding.settingsData!!.m_GameName.trim()
    }

    fun pollForGameConnection() {
        val text = getString(R.string.game_created)
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(m_Context, text, duration)
        toast.show()

        binding.ProgressBarCreateGame.isVisible = true
        binding.startPlaying.isEnabled = false

        //TODO: here polling code

    }

    fun connectedToGame() {
        val text = getString(R.string.connected_togame)
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(m_Context, text, duration)
        toast.show()

        binding.ProgressBarCreateGame.isVisible = false
        binding.startPlaying.isEnabled = true


    }
}