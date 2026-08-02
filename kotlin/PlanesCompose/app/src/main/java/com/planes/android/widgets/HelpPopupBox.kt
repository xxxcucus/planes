package com.planes.android.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens

@Composable
fun HelpPopupBox(modifier: Modifier,
                 currentScreenState: MutableState<String>,
                 showPopupState: MutableState<Boolean>,
                 screenWidth: Float, screenHeight: Float,
                 navController: NavHostController
) {

    var title = ""
    var description = ""
    var showVideoButton = false
    var videoId = 0

    if (currentScreenState.value == PlanesScreens.SinglePlayerBoardEditing.name) {
        title = stringResource(R.string.board_editing_stage)
        description = """
                ${stringResource(R.string.helptext_boardediting_1)}
                ${stringResource(R.string.helptext_boardediting_3)}
                ${stringResource(R.string.helptext_game_3)}
                """.trimIndent()
        showVideoButton = true
        videoId = R.raw.positioning

    } else if (currentScreenState.value == PlanesScreens.MultiplayerBoardEditing.name) {
        title = stringResource(R.string.board_editing_stage)
        description = """
                ${stringResource(R.string.helptext_boardediting_1)}
                ${stringResource(R.string.helptext_boardediting_3)}
                ${stringResource(R.string.helptext_game_3)}
                """.trimIndent()
        showVideoButton = true
        videoId = R.raw.positioning
    } else if (currentScreenState.value == PlanesScreens.SinglePlayerGame.name) {
        title = stringResource(R.string.game_stage)
        description = stringResource(R.string.helptext_game_1)
        showVideoButton = true
        videoId = R.raw.guessing
    } else if (currentScreenState.value == PlanesScreens.MultiplayerGame.name) {
        title = stringResource(R.string.game_stage)
        description = stringResource(R.string.helptext_game_1_opponent)
        showVideoButton = true
        videoId = R.raw.guessing
    } else if (currentScreenState.value == PlanesScreens.SinglePlayerGameNotStarted.name) {
        title = stringResource(R.string.game_not_started_stage)
        description = stringResource(R.string.helptext_startnewgame_1)
        showVideoButton = false
    } else if (currentScreenState.value == PlanesScreens.MultiplayerGameNotStarted.name) {
        title = stringResource(R.string.game_not_started_stage)
        description = stringResource(R.string.helptext_startnewgame_1)
        showVideoButton = false
    } else if (currentScreenState.value == PlanesScreens.Tutorials.name) {
        title = stringResource(R.string.videos)
        description = """
                ${stringResource(R.string.helptext_videos1)}
                ${stringResource(R.string.helptext_videos3)}
                """.trimIndent()
        showVideoButton = false
    } else if (currentScreenState.value == PlanesScreens.Chat.name) {
        title = stringResource(R.string.chat)
        description = stringResource(R.string.helptext_chat)
        showVideoButton = false
    } else if (currentScreenState.value == PlanesScreens.Conversation.name) {
        title = stringResource(R.string.conversation)
        description = """
                ${stringResource(R.string.helptext_conversation1)}
                ${stringResource(R.string.helptext_conversation2)}
                """.trimIndent()
        showVideoButton = false
    }



    PopupBox(modifier,
        popupWidth = screenWidth * 3.0 / 4.0, popupHeight = screenHeight / 2.0, showPopup = showPopupState.value,
        onClickOutside = { showPopupState.value = false }
        ) {
        if (showVideoButton)
            TextPopupWithButton(title = title, description = description,
            stringResource(R.string.popup_help_button_text), videoId, navController)
        else
            TextPopupWithoutButton(title = title, description = description)
    }
}

/*

SinglePlayerBoardEditing,
    SinglePlayerGame,
    SinglePlayerGameNotStarted,
    SinglePlayerGameStatistics,
    CreateMultiplayerGame,
    MultiplayerBoardEditing,
    MultiplayerGame,
    MultiplayerGameNotStarted,
    MultiplayerGameStatistics,
    MultiplayerConnectToGame,
    Info,
    Tutorials,
    Login,
    Register,
    NoRobot,
    DeleteUser,
    Chat,
    Conversation,
    Preferences;

 */