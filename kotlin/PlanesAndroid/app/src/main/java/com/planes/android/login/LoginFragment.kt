package com.planes.android.login

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R
import com.planes.android.databinding.FragmentLoginBinding
import com.planes.android.preferences.MultiplayerPreferencesServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.commobj.LoginCommObj
import com.planes.multiplayer_engine.responses.LoginResponse
import io.reactivex.Observable
import retrofit2.Response


//TODO to update according to google and udemy
class LoginFragment: Fragment() {
    lateinit var binding: FragmentLoginBinding
    private var m_Username = ""
    private var m_Password = ""
    private var m_PreferencesService = MultiplayerPreferencesServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()
    private var m_PlayersListService = PlayersListServiceGlobal()
    private lateinit var m_LoginCommObj: LoginCommObj

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_PreferencesService.createPreferencesService(context)
        m_MultiplayerRound.createPlanesRound()
        m_PlayersListService.createService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.settingsData = LoginViewModel(m_Username, m_Password)

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.login))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Login)
            (activity as MainActivity).updateOptionsMenu()
        }

        val saveSettingsButton = binding.login
        saveSettingsButton.setOnClickListener { performLogin() }

        val goToRegisterButton = binding.register
        goToRegisterButton.setOnClickListener { goToRegistration()}

        val hidePasswordCheckbox = binding.secureCheck
        hidePasswordCheckbox.setOnCheckedChangeListener { _, isChecked ->
            hideShowPassword(
                isChecked
            )
        }

        val useCredentialFromPrefsButton = binding.credentialsPreferences
        useCredentialFromPrefsButton.setOnClickListener {
            binding.settingsData!!.m_Password = m_PreferencesService.password
            binding.settingsData!!.m_Username = m_PreferencesService.username
            binding.invalidateAll()
        }

        val createGameButton = binding.creategame
        createGameButton.isEnabled = !m_MultiplayerRound.getUsername().isEmpty()
        createGameButton.setOnClickListener {
            goToCreateGame()
        }

        return binding.root
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        if (activity is MainActivity)
            return super.onGetLayoutInflater(savedInstanceState)

        val inflater = super.onGetLayoutInflater(savedInstanceState)
        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), R.style.MyAppTheme)
        return inflater.cloneInContext(contextThemeWrapper)
    }

    private fun goToRegistration() {
        if (activity is MainActivity)
            (activity as MainActivity).startRegistrationFragment()
    }

    private fun goToCreateGame() {
        if (activity is MainActivity)
            (activity as MainActivity).startConnectToGameFragment("FromLogin")
    }

    override fun onDetach () {
        super.onDetach()
        hideLoading()
        if (this::m_LoginCommObj.isInitialized)
            m_LoginCommObj.disposeSubscription()
    }

    fun saveCredentials(username: String, password: String, authorizationHeader: String, response: LoginResponse?) {
        m_MultiplayerRound.setUserData(username, password, authorizationHeader)
        if (response != null)
            m_MultiplayerRound.setUserId(response.m_Id.toLong())
        if (activity is MainActivity)
            (activity as MainActivity).showSaveCredentialsPopup(username, password)
        m_PlayersListService.startPolling()
        if (activity is MainActivity)
            (activity as MainActivity).startChatFragment()
    }

    fun saveUserId(response: LoginResponse): String {
        m_MultiplayerRound.setUserId(response.m_Id.toLong())
        return ""
    }


    fun finalizeLoginSuccessful() {
        binding.creategame.isEnabled = true

        if (activity is MainActivity)
            (activity as MainActivity).setUsernameDrawerMenuMultiplayer()
    }

    private fun createObservable() : Observable<Response<LoginResponse>> {
        return m_MultiplayerRound.login(binding.settingsData!!.m_Username.trim(), binding.settingsData!!.m_Password)
    }

    private fun performLogin() {

        if (!this::binding.isInitialized)
            return

        m_MultiplayerRound.setUserData("", "", "")
        if (activity is MainActivity)
            (activity as MainActivity).setUsernameDrawerMenuMultiplayer()

        //TODO: create this in constructor ?
        m_LoginCommObj = LoginCommObj(::createObservable, getString(R.string.loginerror),
            getString(R.string.unknownerror), binding.settingsData!!.m_Username.trim(), binding.settingsData!!.m_Password,
            ::validationUsernamePasswordLogin, ::saveCredentials, ::finalizeLoginSuccessful, requireActivity())

        m_LoginCommObj.makeRequest()
    }

    private fun hideShowPassword(isChecked: Boolean) {
        if (isChecked) {
            binding.passwordEdittext.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            binding.passwordEdittext.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        binding.invalidateAll()
    }


    public fun validationUsernamePasswordLogin(username: String, password: String) : String {
        var retString = ""

        if (username.length > 30) {
            retString += " " + getString(R.string.validation_toolong_login_username)
        }

        if (username.isEmpty()) {
            retString += " " + getString(R.string.validation_empty_login_username)
        }

        if (password.length > 30) {
            retString += " " + getString(R.string.validation_toolong_login_password)
        }

        if (password.isEmpty()) {
            retString += " " + getString(R.string.validation_empty_login_password)
        }

       return retString
    }

    private fun hideLoading() {
        if (activity is MainActivity)
            (activity as MainActivity).stopProgressDialog()
    }
}
