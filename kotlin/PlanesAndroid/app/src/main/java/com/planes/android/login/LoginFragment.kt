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
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R
import com.planes.android.databinding.FragmentLoginBinding
import com.planes.android.preferences.MultiplayerPreferencesServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.commobj.LoginCommObj
import com.planes.multiplayer_engine.responses.ErrorResponse
import com.planes.multiplayer_engine.responses.LoginResponse
import io.reactivex.Observable
import retrofit2.Response


//TODO to update accordint to google and udemy
class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var m_Username = ""
    private var m_Password = ""
    private var m_PreferencesService = MultiplayerPreferencesServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()
    private lateinit var m_LoginCommObj: LoginCommObj

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
        if (this::m_LoginCommObj.isInitialized)
            m_LoginCommObj.disposeSubscription()
    }

    override fun onPause() {
        super.onPause()
    }

    fun saveCredentials(username: String, password: String, authorizationHeader: String) {
        m_MultiplayerRound.setUserData(username, password, authorizationHeader)
        (activity as MainActivity).showSaveCredentialsPopup(username, password)
    }


    fun finalizeLoginSuccessful() {
        (activity as MainActivity).setUsernameDrawerMenuMultiplayer()
    }

    fun createObservable() : Observable<Response<LoginResponse>> {
        return m_MultiplayerRound.login(binding.settingsData!!.m_Username.trim(), binding.settingsData!!.m_Password)
    }

    fun performLogin() {

        if (!this::binding.isInitialized)
            return

        m_MultiplayerRound.setUserData("", "", "")
        (activity as MainActivity).setUsernameDrawerMenuMultiplayer()

        m_LoginCommObj = LoginCommObj(::createObservable, getString(R.string.loginerror),
            getString(R.string.unknownerror), binding.settingsData!!.m_Username.trim(), binding.settingsData!!.m_Password,
            ::validationUsernamePasswordLogin, ::saveCredentials, ::finalizeLoginSuccessful, requireActivity())

        m_LoginCommObj.makeRequest()
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


    fun validationUsernamePasswordLogin(username: String, password: String) : String {
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

       return retString
    }

    fun hideLoading() {
        (activity as MainActivity).stopProgressDialog()
    }
}