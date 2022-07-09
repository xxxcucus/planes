package com.planes.android.gamestats

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
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
    private var m_Username = ""
    private var m_Password = ""
    private var m_MultiplayerRound = MultiplayerRoundJava()
    private lateinit var m_Context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_Context = context
        m_MultiplayerRound.createPlanesRound()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusBinding.inflate(inflater, container, false)
        var username = m_MultiplayerRound.getUsername()
        var gameData = m_MultiplayerRound.getGameData()
        gameData.username = username
        var gameStage = GameStages.get(m_MultiplayerRound.getGameStage())!!
        binding.settingsData = GameStatsViewModel(
            gameData,
            gameStage,
            m_MultiplayerRound.getGameStats(),
            m_Context
        )
        (activity as MainActivity).setActionBarTitle(getString(R.string.game_stats))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.GameStats)

        var loginButton = binding.statusLogin as Button
        loginButton.setOnClickListener(View.OnClickListener { goToLoginScreen() })

        if (!gameData.username.isNullOrEmpty())
            loginButton.isEnabled = false

        var connectToGameButton = binding.statusConnectToGame as Button
        connectToGameButton.setOnClickListener(View.OnClickListener { goToConnectToGameScreen() })

        var connectedToGame = !(gameData.gameName.isNullOrEmpty() ||
                (!gameData.gameName.isNullOrEmpty() && gameData.username == gameData.otherUsername)
                || gameData.roundId == 0L)

        if (gameData.username.isNullOrEmpty() || connectedToGame)
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

    fun goToLoginScreen() {
        (activity as MainActivity).startLoginFragment()
    }

    fun goToConnectToGameScreen() {
        (activity as MainActivity).startConnectToGameFragment()
    }
}