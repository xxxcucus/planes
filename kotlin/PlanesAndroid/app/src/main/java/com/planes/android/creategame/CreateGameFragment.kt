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
import com.planes.android.databinding.FragmentCreateGameBinding
import com.planes.multiplayer_engine.MultiplayerRoundJava
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
        //saveSettingsButton.setOnClickListener(View.OnClickListener { performLogin() })


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
        /*if (this::m_LoginSubscription.isInitialized)
            m_LoginSubscription.dispose()*/
    }

    override fun onPause() {
        super.onPause()
    }

    /*fun checkAuthorization(code: Int, jsonErrorString: String?, headrs: Headers, body: LoginResponse?) {
        if (headrs.get("Authorization") != null) {
            var authorizationHeader = headrs.get("Authorization")
            //TODO: should Bearer be removed from token?

            m_MultiplayerRound.setUserData(
                binding.settingsData!!.m_Username.trim(),
                binding.settingsData!!.m_Password,
                authorizationHeader!!
            )
            (activity as MainActivity).showSaveCredentialsPopup(binding.settingsData!!.m_Username.trim(),
                binding.settingsData!!.m_Password,
            )

        } else {
            if (jsonErrorString != null) {
                var gson = Gson()
                var errorResponse = gson?.fromJson(jsonErrorString, ErrorResponse::class.java)

                if (errorResponse != null)
                    m_CreateGameErrorString = getString(R.string.loginerror) + ":" + errorResponse.m_Message + "(" + errorResponse.m_Status + ")"
                else
                    m_CreateGameErrorString = getString(R.string.loginerror) + ":" + getString(R.string.unknownerror)
            } else {
                m_CreateGameErrorString = getString(R.string.loginerror) + ":" + getString(R.string.unknownerror)
            }
            m_CreateGameError = true
        }
        //finalizeLogin()
    }*/

    fun setCreateGameError(errorMsg: String) {
        m_CreateGameError = true
        m_CreateGameErrorString = errorMsg
        //finalizeLogin()
    }

    /*fun finalizeLogin() {
        if (m_CreateGameError) {
            (activity as MainActivity).onWarning(m_CreateGameErrorString)
        } else {
            (activity as MainActivity).setUsernameDrawerMenuMultiplayer()
        }
    }*/

    fun performLogin() {

        if (!this::binding.isInitialized)
            return

        m_CreateGameError = false
        m_CreateGameErrorString = ""

        if (!validationGameName(binding.settingsData!!.m_GameName.trim())) {
            //finalizeLogin()
            return
        }

        /*var login = m_MultiplayerRound.login(binding.settingsData!!.m_Username.trim(), binding.settingsData!!.m_Password)
        m_LoginSubscription = login
            .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> showLoading() }
            .doOnTerminate { hideLoading() }
            .doOnComplete { hideLoading() }
            .subscribe({data -> checkAuthorization(data.code(), data.errorBody()?.string(), data.headers(), data.body())}
                , {error -> setLoginError(error.localizedMessage.toString())});*/
    }

    fun showLoading() {
        (activity as MainActivity).startProgressDialog()
    }

    fun hideLoading() {
        (activity as MainActivity).stopProgressDialog()
    }

    fun validationGameName(gameName: String) : Boolean {
        var retString = ""

        /*if (username.length > 30) {
            retString += " " + getString(R.string.validation_toolong_login_username)
        }

        if (username.isNullOrEmpty()) {
            retString += " " + getString(R.string.validation_empty_login_username)
        }

        if (password.length > 30) {
            retString += " " + getString(R.string.validation_toolong_login_password)
        }

        if (password.isNullOrEmpty()) {
            retString += " " + getString(R.string.validation_empty_login_password)
        }

        if (!retString.isNullOrEmpty()) {
            m_LoginError = true
            m_LoginErrorString = retString
            return false
        }*/
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
}