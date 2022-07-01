package com.planes.android.register

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
import com.planes.android.databinding.FragmentRegisterBinding
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.commobj.LoginCommObj
import com.planes.multiplayer_engine.commobj.RegisterCommObj
import com.planes.multiplayer_engine.responses.ErrorResponse
import com.planes.multiplayer_engine.responses.RegistrationResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.TimeUnit

//TODO to update accordint to google and udemy
class RegisterFragment: Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private var m_Username = ""
    private var m_Password = ""

    private var m_MultiplayerRound = MultiplayerRoundJava()
    private lateinit var m_RegisterCommObj: RegisterCommObj

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
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.settingsData = RegisterViewModel(m_Username, m_Password)
        (activity as MainActivity).setActionBarTitle(getString(R.string.register))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Register)

        var saveSettingsButton = binding.register as Button
        saveSettingsButton.setOnClickListener(View.OnClickListener { performRegister() })

        var hidePasswordCheckbox = binding.secureCheck as CheckBox
        hidePasswordCheckbox.setOnCheckedChangeListener(
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> hideShowPassword(buttonView, isChecked) })

        return binding.root
    }

    override fun onDetach () {
        super.onDetach()
        hideLoading()
        if (this::m_RegisterCommObj.isInitialized)
            m_RegisterCommObj.disposeSubscription()
    }

    override fun onPause() {
        super.onPause()
    }

    fun prepareNorobotTest(body: RegistrationResponse): String {
        m_MultiplayerRound.setRegistrationResponse(body)
        return ""
    }

    fun finalizeRegister() {
        (activity as MainActivity).startNoRobotFragment(m_MultiplayerRound.getRegistrationResponse())
    }

    fun createObservableRegister() : Observable<Response<RegistrationResponse>> {
        return m_MultiplayerRound.register(binding.settingsData!!.m_Username.trim(), binding.settingsData!!.m_Password)
    }

    fun performRegister() {

        if (!this::binding.isInitialized)
            return

        m_RegisterCommObj = RegisterCommObj(::hideLoading, ::showLoading, ::createObservableRegister, getString(R.string.loginerror),
            getString(R.string.unknownerror), binding.settingsData!!.m_Username.trim(), binding.settingsData!!.m_Password,
            ::validationUsernamePasswordRegister, ::prepareNorobotTest, ::finalizeRegister, requireActivity())

        m_RegisterCommObj.makeRequest()
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

    //TODO: to move in BasisCommObj
    fun showLoading() {
        (activity as MainActivity).startProgressDialog()
    }

    fun hideLoading() {
        (activity as MainActivity).stopProgressDialog()
    }

    fun validationUsernamePasswordRegister(username: String, password: String) : String {
        var retString = ""

        if (username.length > 30) {
            retString += " " + getString(R.string.validation_toolong_login_username)
        }

        if (username != username.trim()) {
            retString += " " + getString(R.string.validation_startsendswithempty_register_username)
        }

        if (username.length < 5) {
            retString += " " + getString(R.string.validation_tooshort_register_username)
        }

        if (username.isNullOrEmpty()) {
            retString += " " + getString(R.string.validation_empty_login_username)
        }

        if (password.length > 30) {
            retString += " " + getString(R.string.validation_toolong_login_password)
        }

        if (password.length < 5) {
            retString += " " + getString(R.string.validation_tooshort_register_password)
        }

        if (password.isNullOrEmpty()) {
            retString += " " + getString(R.string.validation_empty_login_password)
        }

        return retString
    }
}