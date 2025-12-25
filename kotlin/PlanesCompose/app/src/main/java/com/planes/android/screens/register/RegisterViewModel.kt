package com.planes.android.screens.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.repository.PlanesUserRepository
import com.planes.android.screens.norobot.PhotoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: PlanesUserRepository): ViewModel() {

    private var m_UserName = mutableStateOf("")
    private var m_Password = mutableStateOf("")
    private var m_Loading = mutableStateOf(false)

    private var m_PendingUserName = mutableStateOf<String?>(null)
    private var m_PendingUserId = mutableStateOf<String?>(null)
    private var m_Question = mutableStateOf<String?>(null)
    private var m_NoRobotImage1 = mutableStateOf<String?>(null)
    private var m_NoRobotImage2 = mutableStateOf<String?>(null)
    private var m_NoRobotImage3 = mutableStateOf<String?>(null)
    private var m_NoRobotImage4 = mutableStateOf<String?>(null)
    private var m_NoRobotImage5 = mutableStateOf<String?>(null)
    private var m_NoRobotImage6 = mutableStateOf<String?>(null)
    private var m_NoRobotImage7 = mutableStateOf<String?>(null)
    private var m_NoRobotImage8 = mutableStateOf<String?>(null)
    private var m_NoRobotImage9 = mutableStateOf<String?>(null)

    private var m_Error = mutableStateOf<String?>(null)

    fun getUserName(): String {
        return m_UserName.value
    }

    fun setUserName(value: String) {
        m_UserName.value = value
    }

    fun getPassword(): String {
        return m_Password.value
    }

    fun setPassword(value: String) {
        m_Password.value = value
    }

    fun getLoading(): Boolean {
        return m_Loading.value
    }

    fun setLoading(value: Boolean) {
        m_Loading.value = value
    }

    fun getPendingUserId(): String? {
        return m_PendingUserId.value
    }

    fun setPendingUserId(value: String?) {
        m_PendingUserId.value = value
    }

    fun getPendingUsername(): String? {
        return m_PendingUserName.value
    }

    fun setPendingUsername(value: String?) {
        m_PendingUserName.value = value
    }

    fun getError(): String? {
        return m_Error.value
    }

    fun setError(value: String?) {
        m_Error.value = value
    }

    fun getQuestion(): String? {
        return m_Question.value
    }

    fun setQuestion(value: String?) {
        m_Question.value = value
    }

    fun register() {
        viewModelScope.launch {
            m_Loading.value = true
            m_Error.value = null
            val result = withContext(Dispatchers.IO) {
                val bchash = BCrypt.hashpw(getPassword(), BCrypt.gensalt())
                repository.register(getUserName(), bchash)
            }

            if (result.data == null) {
                Log.d("PlaneCompose", "Register error ${result.e}")
                m_Error.value = result.e
            } else {
                m_PendingUserId.value = result.data?.m_Id
                m_PendingUserName.value = result.data?.m_Username
                m_NoRobotImage1.value = result.data?.m_ImageId_1
                m_NoRobotImage2.value = result.data?.m_ImageId_2
                m_NoRobotImage3.value = result.data?.m_ImageId_3
                m_NoRobotImage4.value = result.data?.m_ImageId_4
                m_NoRobotImage5.value = result.data?.m_ImageId_5
                m_NoRobotImage6.value = result.data?.m_ImageId_6
                m_NoRobotImage7.value = result.data?.m_ImageId_7
                m_NoRobotImage8.value = result.data?.m_ImageId_8
                m_NoRobotImage9.value = result.data?.m_ImageId_9

                m_Question.value = result.data?.m_Question

                Log.d("PlanesCompose", "Registration request successfull with id ${getPendingUserId()}, username ${getPendingUsername()} ")
            }
            m_Loading.value = result.loading!!
        }
    }

    fun noRobotDataAvailable() : Boolean {
        if (m_PendingUserId.value == null || m_PendingUserName.value == null)
            return false

        if (m_NoRobotImage1.value == null || m_NoRobotImage2.value == null
            || m_NoRobotImage3.value == null)
            return false

        if (m_NoRobotImage4.value == null || m_NoRobotImage5.value == null
            || m_NoRobotImage6.value == null)
            return false

        if (m_NoRobotImage7.value == null || m_NoRobotImage8.value == null
            || m_NoRobotImage9.value == null)
            return false

        if (m_Question.value == null)
            return false

        return true
    }

    fun getPhotoItems(): List<String> {
        if (!noRobotDataAvailable())
            return emptyList<String>()

        val retVal = mutableListOf<String>()

        retVal.add(m_NoRobotImage1.value!!)
        retVal.add(m_NoRobotImage2.value!!)
        retVal.add(m_NoRobotImage3.value!!)
        retVal.add(m_NoRobotImage4.value!!)
        retVal.add(m_NoRobotImage5.value!!)
        retVal.add(m_NoRobotImage6.value!!)
        retVal.add(m_NoRobotImage7.value!!)
        retVal.add(m_NoRobotImage8.value!!)
        retVal.add(m_NoRobotImage9.value!!)

        return retVal
    }
}