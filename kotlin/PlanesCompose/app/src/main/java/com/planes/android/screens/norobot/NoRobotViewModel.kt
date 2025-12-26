package com.planes.android.screens.norobot

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.network.user.requests.NoRobotRequest
import com.planes.android.repository.PlanesUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

@HiltViewModel
class NoRobotViewModel @Inject constructor(private val repository: PlanesUserRepository): ViewModel()  {

    private val m_PhotoList = mutableStateListOf<PhotoModel>()
    private var m_Question = mutableStateOf<String?>(null)
    private var m_RequestId = mutableStateOf<String?>(null)
    private var m_Loading = mutableStateOf(false)
    private var m_Error = mutableStateOf<String?>(null)

    private var m_PendingUserName = mutableStateOf<String?>(null)
    private var m_PendingUserId = mutableStateOf<String?>(null)
    private var m_PendingStatus = mutableStateOf<String?>(null)
    private var m_PendingCreatedAt = mutableStateOf<String?>(null)

    fun setImages(entries: List<PhotoModel>) {
        m_PhotoList.clear()
        entries.forEach {
            m_PhotoList.add(it)
        }
    }

    fun getImages(): List<PhotoModel> {
        return m_PhotoList.toList()
    }

    fun getImage(index: Int): PhotoModel {
        return m_PhotoList.toList()[index]
    }

    fun getLoading(): Boolean {
        return m_Loading.value
    }

    fun setLoading(value: Boolean) {
        m_Loading.value = value
    }

    fun getQuestion(): String? {
        return m_Question.value
    }

    fun setQuestion(value: String?) {
        m_Question.value = value
    }

    fun getRequestId(): String? {
        return m_RequestId.value
    }
    fun setRequestId(value: String?) {
        m_RequestId.value = value
    }

    fun setError(value: String?) {
        m_Error.value = value
    }

    fun getError() : String? {
        return m_Error.value
    }

    fun setUserId(value: String?) {
        m_PendingUserId.value = value
    }

    fun getUserId(): String? {
        return m_PendingUserId.value
    }

    fun setUserName(value: String?) {
        m_PendingUserName.value = value
    }

    fun getUserName(): String? {
        return m_PendingUserName.value
    }

    fun setStatus(value: String?) {
        m_PendingStatus.value = value
    }

    fun getStatus(): String? {
        return m_PendingStatus.value
    }

    fun setCreatedAt(value: String?) {
        m_PendingCreatedAt.value = value
    }

    fun getCreatedAt(): String? {
        return m_PendingCreatedAt.value
    }

    fun toggleSelected(index: Int) {
        val photoModel = m_PhotoList[index]
        m_PhotoList.removeRange(index, index + 1)
        photoModel.m_Selected = !photoModel.m_Selected
        m_PhotoList.add(index, photoModel)
    }

    fun getSelected(index: Int): Boolean {
        return m_PhotoList[index].m_Selected
    }

    fun getAnswer(): String {

        var answer = ""

        for (i in m_PhotoList.indices) {
            answer += if (m_PhotoList[i].m_Selected) "1" else "0"
        }

        return answer
    }

    fun noRobotRequest() {
        viewModelScope.launch {
            m_Loading.value = true
            m_Error.value = null
            val result = withContext(Dispatchers.IO) {
                val request = NoRobotRequest(getRequestId()!!, getAnswer())
                repository.noRobotRequest(request)
            }

            if (result.data == null) {
                Log.d("PlaneCompose", "No Robot error ${result.e}")
                m_Error.value = result.e
            } else {
                m_PendingUserId.value = result.data?.m_UserId
                m_PendingUserName.value = result.data?.m_Username
                m_PendingStatus.value = result.data?.m_Status
                m_PendingCreatedAt.value = result.data?.m_CreatedAt


                Log.d("PlanesCompose", "No Robot request successfull with id ${getUserId()}, username ${getUserName()} ")
            }
            m_Loading.value = result.loading!!
        }
    }

    fun responseAvailable() : Boolean {
        if (m_PendingUserId.value == null || m_PendingUserName.value == null)
            return false

        if (m_PendingStatus.value == null || m_PendingCreatedAt.value == null)
            return false

        return true
    }

}