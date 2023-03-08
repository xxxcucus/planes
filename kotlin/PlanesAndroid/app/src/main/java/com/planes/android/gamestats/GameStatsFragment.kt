package com.planes.android.gamestats

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R
import com.planes.android.databinding.FragmentStatusBinding
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.single_player_engine.GameStages


class GameStatsFragment: Fragment() {
    private lateinit var binding: FragmentStatusBinding
    public var m_MultiplayerRound = MultiplayerRoundJava()
    public lateinit var m_Context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_Context = context
        m_MultiplayerRound.createPlanesRound()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatusBinding.inflate(inflater, container, false)
        val username = m_MultiplayerRound.getUsername()
        val gameData = m_MultiplayerRound.getGameData()
        gameData.username = username
        val gameStage = GameStages[m_MultiplayerRound.getGameStage()]!!
        binding.settingsData = GameStatsViewModel(
            gameData,
            gameStage,
            m_MultiplayerRound.getGameStats(),
            m_Context
        )

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.game_stats))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.GameStats)
            (activity as MainActivity).updateOptionsMenu()
        }

        val loginButton = binding.statusLogin
        loginButton.setOnClickListener { goToLoginScreen() }

        if (gameData.username.isNotEmpty())
            loginButton.isEnabled = false

        val connectToGameButton = binding.statusConnectToGame
        connectToGameButton.setOnClickListener { goToConnectToGameScreen() }

        val connectedToGame = !(gameData.gameName.isEmpty() ||
                (gameData.gameName.isNotEmpty() && gameData.username == gameData.otherUsername)
                || gameData.roundId == 0L)

        if (gameData.username.isEmpty() || connectedToGame)
            connectToGameButton.isEnabled = false

        if (!connectedToGame) {
            binding.statusOpponent.isVisible = false
            binding.statusCurrentRound.isVisible = false
            binding.statusRoundStatus.isVisible = false
            binding.statusGameScore.isVisible = false
        }

        if (gameStage != GameStages.Game && gameStage != GameStages.WaitForOpponentMoves && gameStage != GameStages.SendRemainingMoves) {
            binding.statusGamestats.isVisible = false
        }

        return binding.root
    }

    private fun goToLoginScreen() {
        if (activity is MainActivity)
            (activity as MainActivity).startLoginFragment()
    }

    private fun goToConnectToGameScreen() {
        if (activity is MainActivity)
            (activity as MainActivity).startConnectToGameFragment()
    }
}