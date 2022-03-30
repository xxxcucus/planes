package com.planes.android.login

import android.content.Context
import android.os.Bundle
import android.text.InputType
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
import com.planes.android.databinding.FragmentLoginBinding
import com.planes.android.preferences.MultiplayerPreferencesServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.ErrorResponse
import com.planes.multiplayer_engine.responses.LoginResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import java.util.concurrent.TimeUnit

//TODO to update accordint to google and udemy
class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var m_Username = ""
    private var m_Password = ""
    private var m_LoginError = false
    private var m_LoginErrorString = ""
    private var m_PreferencesService = MultiplayerPreferencesServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()
    private lateinit var m_LoginSubscription: Disposable

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_PreferencesService.createPreferencesService(context)
        m_MultiplayerRound.createPlanesRound()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.settingsData = LoginViewModel(m_Username, m_Password)
        (activity as MainActivity).setActionBarTitle(getString(R.string.login))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Login)

        var saveSettingsButton = binding.login as Button
        saveSettingsButton.setOnClickListener(View.OnClickListener { performLogin() })

        var hidePasswordCheckbox = binding.secureCheck as CheckBox
        hidePasswordCheckbox.setOnCheckedChangeListener(
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> hideShowPassword(buttonView, isChecked) })

        var useCredentialFromPrefsButton = binding.credentialsPreferences
        useCredentialFromPrefsButton.setOnClickListener {
            binding.settingsData!!.m_Password = m_PreferencesService.password
            binding.settingsData!!.m_Username = m_PreferencesService.username
            binding.invalidateAll()
        }

        return binding.root
    }

    override fun onDetach () {
        super.onDetach()
        hideLoading()
        if (this::m_LoginSubscription.isInitialized)
            m_LoginSubscription.dispose()
    }

    override fun onPause() {
        super.onPause()
    }

    fun checkAuthorization(code: Int, jsonErrorString: String?, headrs: Headers, body: LoginResponse?) {
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
            m_LoginErrorString = Tools.parseJsonError(jsonErrorString, getString(R.string.loginerror),
                getString(R.string.unknownerror))
            m_LoginError = true
        }
        finalizeLogin()
    }

    fun setLoginError(errorMsg: String) {
        m_LoginError = true
        m_LoginErrorString = errorMsg
        finalizeLogin()
    }

    fun finalizeLogin() {
        if (m_LoginError) {
            (activity as MainActivity).onWarning(m_LoginErrorString)
        } else {
            (activity as MainActivity).setUsernameDrawerMenuMultiplayer()
        }
    }

    fun performLogin() {

        if (!this::binding.isInitialized)
            return

        m_LoginError = false
        m_LoginErrorString = ""

        if (!validationUsernamePasswordLogin(binding.settingsData!!.m_Username.trim(), binding.settingsData!!.m_Password)) {
            finalizeLogin()
            return
        }

        var login = m_MultiplayerRound.login(binding.settingsData!!.m_Username.trim(), binding.settingsData!!.m_Password)
        m_LoginSubscription = login
            .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> showLoading() }
            .doOnTerminate { hideLoading() }
            .doOnComplete { hideLoading() }
            .subscribe({data -> checkAuthorization(data.code(), data.errorBody()?.string(), data.headers(), data.body())}
                , {error -> setLoginError(error.localizedMessage.toString())});
    }

    fun hideShowPassword(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            binding.passwordEdittext.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            binding.passwordEdittext.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        binding.invalidateAll()
    }

    fun showLoading() {
        (activity as MainActivity).startProgressDialog()
    }

    fun hideLoading() {
        (activity as MainActivity).stopProgressDialog()
    }

    fun validationUsernamePasswordLogin(username: String, password: String) : Boolean {
        var retString = ""

        if (username.length > 30) {
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
        }
        return true
    }
}