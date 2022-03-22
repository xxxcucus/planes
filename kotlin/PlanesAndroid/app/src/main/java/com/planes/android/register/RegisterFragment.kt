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
import com.planes.android.databinding.FragmentRegisterBinding
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.ErrorResponse
import com.planes.multiplayer_engine.responses.RegistrationResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

//TODO to update accordint to google and udemy
class RegisterFragment: Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private var m_Username = ""
    private var m_Password = ""
    private var m_RegisterError = false
    private var m_RegisterErrorString = ""
    private var m_MultiplayerRound = MultiplayerRoundJava()
    private lateinit var m_RegisterSubscription: Disposable

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
        if (this::m_RegisterSubscription.isInitialized)
            m_RegisterSubscription.dispose()
    }

    override fun onPause() {
        super.onPause()
    }

    fun prepareNorobotTest(code: Int, jsonErrorString: String?, body: RegistrationResponse?) {

        if (body != null) {
            m_MultiplayerRound.setRegistrationResponse(body!!)
        } else {
            if (jsonErrorString != null) {
                var gson = Gson()
                var errorResponse = gson?.fromJson(jsonErrorString, ErrorResponse::class.java)

                if (errorResponse != null)
                    m_RegisterErrorString =
                        getString(R.string.registererror) + ":" + errorResponse.m_Message + "(" + errorResponse.m_Status + ")"
                else
                    m_RegisterErrorString =
                        getString(R.string.registererror) + ":" + getString(R.string.unknownerror)
            } else {
                m_RegisterErrorString =
                    getString(R.string.registererror) + ":" + getString(R.string.unknownerror)
            }
            m_RegisterError = true
        }


        finalizeRegister()
    }

    fun setRegisterError(errorMsg: String) {
        m_RegisterError = true
        m_RegisterErrorString = errorMsg
        finalizeRegister()
    }

    fun finalizeRegister() {
        if (m_RegisterError) {
            (activity as MainActivity).onWarning(m_RegisterErrorString)
        } else {
            (activity as MainActivity).startNoRobotFragment(m_MultiplayerRound.getRegistrationResponse())
        }
    }

    fun performRegister() {

        if (!this::binding.isInitialized)
            return

        m_RegisterError = false
        m_RegisterErrorString = ""

        if (!validationUsernamePasswordRegister(binding.settingsData!!.m_Username, binding.settingsData!!.m_Password)) {
            finalizeRegister()
            return
        }

        var register = m_MultiplayerRound.register(binding.settingsData!!.m_Username, binding.settingsData!!.m_Password)
        m_RegisterSubscription = register
            .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> showLoading() }
            .doOnTerminate { hideLoading() }
            .doOnComplete { hideLoading() }
            .subscribe({data -> prepareNorobotTest(data.code(), data.errorBody()?.string(), data.body())}
                , {error -> setRegisterError(error.localizedMessage.toString())});
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

    fun validationUsernamePasswordRegister(username: String, password: String) : Boolean {
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

        if (!retString.isNullOrEmpty()) {
            m_RegisterError = true
            m_RegisterErrorString = retString
            return false
        }
        return true
    }
}