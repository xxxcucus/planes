package com.planes.android.screens.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.repository.PlanesVersionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val repository: PlanesVersionRepository): ViewModel() {

    private var m_ServerOnline = mutableStateOf(false)
    private var m_CheckEnded = mutableStateOf(false)

    fun checkPlanesVersion() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getVersion()
            }

            m_ServerOnline.value = result.data != null
            m_CheckEnded.value = true
        }
    }

    fun isServerOnline(): Boolean {
        return m_ServerOnline.value
    }

    fun isCheckEnded(): Boolean {
        return m_CheckEnded.value
    }

}