package com.planes.android.screens.norobot

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.planes.android.repository.PlanesUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoRobotViewModel @Inject constructor(private val repository: PlanesUserRepository): ViewModel()  {

    private val m_PhotoList = mutableStateListOf<PhotoModel>()
    private var m_Question = mutableStateOf<String?>(null)
    private var m_Loading = mutableStateOf(false)

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

    fun toggleSelected(index: Int) {
        val photoModel = m_PhotoList[index]
        m_PhotoList.removeRange(index, index + 1)
        photoModel.m_Selected = !photoModel.m_Selected
        m_PhotoList.add(index, photoModel)
    }

    fun getSelected(index: Int): Boolean {
        return m_PhotoList[index].m_Selected
    }

}