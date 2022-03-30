package com.planes.android.creategame

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R
import com.planes.android.Tools
import com.planes.android.databinding.FragmentCreateGameBinding
import com.planes.multiplayer_engine.MultiplayerRoundJava
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_MultiplayerRound.createPlanesRound()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateGameBinding.inflate(inflater, container, false)
        binding.settingsData = CreateGameViewModel(m_GameName)
        (activity as MainActivity).setActionBarTitle(getString(R.string.create_connectto_game))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.CreateGame)

        var createGameButton = binding.creategame as Button
        createGameButton.setOnClickListener(View.OnClickListener { checkGameStatus() })


        var generateGameNameButton = binding.generateGamename
        generateGameNameButton.setOnClickListener {
            binding.settingsData!!.m_GameName = generateRandonGameName()
            binding.invalidateAll()
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
           
        } else {
            m_CreateGameErrorString = Tools.parseJsonError(jsonErrorString, getString(R.string.creategame_error),
                getString(R.string.unknownerror))
            m_CreateGameError = true
        }
        finalizeCreateGame()
    }

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
}