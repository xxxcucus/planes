package com.planes.android.gamestats

import android.content.Context
import androidx.lifecycle.ViewModel
import com.planes.android.R
import com.planes.single_player_engine.GameStages
import com.planes.single_player_engine.GameStatistics

class GameStatsViewModel(var username: String, var gameName: String, var roundId: String, var opponent:String,
                         var gameStage: GameStages, gameStats: GameStatistics, context: Context
):  ViewModel() {

    var m_UserLoggedIn: Boolean
    var m_UserName: String
    var m_ConnectedToGame: Boolean
    var m_GameName: String
    var m_RoundId: String
    var m_OpponentName: String
    var m_GameStatsShown: Boolean
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
        m_UserLoggedIn = if (username.isNullOrEmpty()) false else true
        m_UserName = username
        m_ConnectedToGame = if (gameName.isNullOrEmpty()) false else true
        m_GameName = gameName
        m_RoundId = roundId
        m_OpponentName = opponent
        m_GameStage = ""
        m_GameStatsShown = gameStage == GameStages.WaitForOpponentMoves || gameStage == GameStages.Game
        when (gameStage) {
            GameStages.GameNotStarted -> m_GameStage = context.resources.getString(R.string.game_not_started_stage)
            GameStages.BoardEditing -> m_GameStage = context.resources.getString(R.string.board_editing_stage)
            GameStages.Game -> m_GameStage = context.resources.getString(R.string.game)
            GameStages.WaitForOpponentPlanesPositions -> m_GameStage = context.resources.getString(R.string.board_editing_stage)
            GameStages.WaitForOpponentMoves -> m_GameStage = context.resources.getString(R.string.game)
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
        m_Context = context
    }
}