package com.planes.android.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
fun HelpPopupBox(modifier: Modifier,
            currentScreenState: MutableState<String>,
               showPopupState: MutableState<Boolean>,
                 screenWidth: Float, screenHeight: Float) {
    PopupBox(modifier,
        popupWidth = screenWidth * 3.0 / 4.0, popupHeight = screenHeight / 2.0, showPopup = showPopupState.value,
        onClickOutside = { showPopupState.value = false }
        ) { TextPopupWithButton(title = "Help",
        description = currentScreenState.value,
            "See More")}
}

