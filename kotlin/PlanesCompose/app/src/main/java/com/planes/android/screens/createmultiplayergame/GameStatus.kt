package com.planes.android.screens.createmultiplayergame

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class GameStatus {
    private var m_GameId = mutableStateOf<String?>(null)
    private var m_GameName = mutableStateOf<String?>(null)
    private var m_Exists = mutableStateOf<Boolean?>(null)
    private var m_FirstPlayerName = mutableStateOf<String?>(null)
    private var m_FirstPlayerId = mutableStateOf<String?>(null)
    private var m_SecondPlayerName = mutableStateOf<String?>(null)
    private var m_SecondPlayerId = mutableStateOf<String?>(null)
    private var m_CurrentRoundId = mutableStateOf<String?>(null)

    fun getExists(): Boolean? {
        return m_Exists.value
    }

    fun setExists(value: Boolean?) {
        m_Exists.value = value
    }

    fun getGameId(): String? {
        return m_GameId.value
    }

    fun getGameIdState(): MutableState<String?> {
        return m_GameId
    }

    fun setGameId(value: String?) {
        m_GameId.value = value
    }

    fun getGameName(): String? {
        return m_GameName.value
    }

    fun getGameNameState(): MutableState<String?> {
        return m_GameName
    }

    fun setGameName(value: String?) {
        m_GameName.value = value
    }

    fun getFirstPlayerName(): String? {
        return m_FirstPlayerName.value
    }

    fun getFirstPlayerNameState(): MutableState<String?> {
        return m_FirstPlayerName
    }

    fun setFirstPlayerName(value: String?) {
        m_FirstPlayerName.value = value
    }

    fun getFirstPlayerId(): String? {
        return m_FirstPlayerId.value
    }

    fun getFirstPlayerIdState(): MutableState<String?> {
        return m_FirstPlayerId
    }

    fun setFirstPlayerId(value: String?) {
        m_FirstPlayerId.value = value
    }

    fun getSecondPlayerName(): String? {
        return m_SecondPlayerName.value
    }

    fun getSecondPlayerNameState(): MutableState<String?> {
        return m_SecondPlayerName
    }

    fun setSecondPlayerName(value: String?) {
        m_SecondPlayerName.value = value
    }

    fun getSecondPlayerId(): String? {
        return m_SecondPlayerId.value
    }

    fun getSecondPlayerIdState(): MutableState<String?> {
        return m_SecondPlayerId
    }

    fun setSecondPlayerId(value: String?) {
        m_SecondPlayerId.value = value
    }

    fun getCurrentRoundId(): String? {
        return m_CurrentRoundId.value
    }

    fun getCurrentRoundIdState(): MutableState<String?> {
        return m_CurrentRoundId
    }

    fun setCurrentRoundId(value: String?) {
        m_CurrentRoundId.value = value
    }
}