package com.planes.android.gamestats

import android.content.Context
import androidx.lifecycle.ViewModel
import com.planes.android.R
import com.planes.multiplayer_engine.GameData
import com.planes.single_player_engine.GameStages
import com.planes.single_player_engine.GameStatistics

class GameStatsViewModel(gameData: GameData, gameStage: GameStages, gameStats: GameStatistics, context: Context
):  ViewModel() {

    private var m_UserLoggedIn: Boolean
    var m_LoginStatus: String
    var m_UserName: String
    var m_ConnectStatus: String
    private var m_ConnectedToGame: Boolean
    var m_GameName: String
    var m_RoundId: String
    var m_OpponentName: String
    private var m_GameStatsShown: Boolean
    var m_GameStage: String
    var m_PlayerMoves: String
    var m_PlayerHits: String
    var m_PlayerDead: String
    var m_PlayerMisses: String
    var m_OpponentMoves: String
    var m_OpponentHits: String
    var m_OpponentDead: String
    var m_OpponentMisses: String
    var m_PlayerWins: String
    var m_OpponentWins: String
    var m_Draws: String
    var m_Context: Context

    init  {
        m_Context = context

        m_UserLoggedIn = gameData.username.isNotEmpty()
        m_LoginStatus = if (m_UserLoggedIn) m_Context.resources.getString(R.string.userloggedin) else m_Context.resources.getString(R.string.nouser)
        m_UserName = if (m_UserLoggedIn) gameData.username else ""
        m_ConnectedToGame = gameData.gameName.isNotEmpty()
        m_GameName = gameData.gameName
        val connectedToGame = !(gameData.gameName.isEmpty() || (gameData.gameName.isNotEmpty() && gameData.username == gameData.otherUsername)
                || gameData.roundId == 0L)
        if (!connectedToGame) {
            m_ConnectStatus = m_Context.resources.getString(R.string.not_connected_togame)
            m_GameName = ""
        } else {
            m_ConnectStatus = m_Context.resources.getString(R.string.connected_togame)
            m_GameName = gameData.gameName
        }
        m_RoundId = gameData.roundId.toString()
        m_OpponentName = gameData.otherUsername
        m_GameStage = ""
        m_GameStatsShown = gameStage == GameStages.WaitForOpponentMoves || gameStage == GameStages.Game || gameStage == GameStages.SendRemainingMoves
        when (gameStage) {
            GameStages.GameNotStarted -> m_GameStage = context.resources.getString(R.string.game_not_started_stage)
            GameStages.BoardEditing -> m_GameStage = context.resources.getString(R.string.board_editing_stage)
            GameStages.Game -> m_GameStage = context.resources.getString(R.string.game)
            GameStages.WaitForOpponentPlanesPositions -> m_GameStage = context.resources.getString(R.string.board_editing_stage)
            GameStages.WaitForOpponentMoves -> m_GameStage = context.resources.getString(R.string.game)
            GameStages.SendRemainingMoves -> m_GameStage = context.resources.getString(R.string.game)
        }
        m_PlayerMoves = gameStats.m_playerMoves.toString()
        m_PlayerDead = gameStats.m_playerDead.toString()
        m_PlayerHits = gameStats.m_playerHits.toString()
        m_PlayerMisses = gameStats.m_playerMisses.toString()
        m_PlayerWins = gameStats.m_playerWins.toString()
        m_OpponentMoves = gameStats.m_computerMoves.toString()
        m_OpponentDead = gameStats.m_computerDead.toString()
        m_OpponentHits = gameStats.m_computerHits.toString()
        m_OpponentMisses = gameStats.m_computerMisses.toString()
        m_OpponentWins = gameStats.m_computerWins.toString()
        m_Draws = gameStats.m_draws.toString()

    }
}