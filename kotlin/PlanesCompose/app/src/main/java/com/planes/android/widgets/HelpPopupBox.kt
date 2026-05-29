package com.planes.android.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens

@Composable
fun HelpPopupBox(modifier: Modifier,
                 currentScreenState: MutableState<String>,
                 showPopupState: MutableState<Boolean>,
                 screenWidth: Float, screenHeight: Float) {

    var title = ""

    if (currentScreenState.value == PlanesScreens.SinglePlayerBoardEditing.name) {
        title = stringResource(R.string.board_editing_stage)
    } else if (currentScreenState.value == PlanesScreens.MultiplayerBoardEditing.name) {
        title = stringResource(R.string.board_editing_stage)
    } else if (currentScreenState.value == PlanesScreens.SinglePlayerGame.name) {
        title = stringResource(R.string.game_stage)
    } else if (currentScreenState.value == PlanesScreens.MultiplayerGame.name) {
        title = stringResource(R.string.game_stage)
    } else if (currentScreenState.value == PlanesScreens.SinglePlayerGameNotStarted.name) {
        title = stringResource(R.string.game_not_started_stage)
    } else if (currentScreenState.value == PlanesScreens.MultiplayerGameNotStarted.name) {
        title = stringResource(R.string.game_not_started_stage)
    } else if (currentScreenState.value == PlanesScreens.Tutorials.name) {
        title = stringResource(R.string.videos)
    } else if (currentScreenState.value == PlanesScreens.Chat.name) {
        title = stringResource(R.string.chat)
    } else if (currentScreenState.value == PlanesScreens.Conversation.name) {
        title = stringResource(R.string.conversation)
    }

    var description = ""

    PopupBox(modifier,
        popupWidth = screenWidth * 3.0 / 4.0, popupHeight = screenHeight / 2.0, showPopup = showPopupState.value,
        onClickOutside = { showPopupState.value = false }
        ) { TextPopupWithButton(title = title,
        description = description,
            "See More")}
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