package com.planes.android.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun HelpPopupBox(currentScreenState: MutableState<String>,
               showPopupState: MutableState<Boolean>  ) {
    PopupBox(popupWidth = 200f, popupHeight = 100f, showPopup = showPopupState.value,
        onClickOutside = { showPopupState.value = false }
        ) { TextPopupWithButton(title = "Help",
        description = currentScreenState.value,
            "See More")}
}

@Composable
fun TextPopupWithButton(title: String, description: String, buttonText: String) {
    Column() {
        Text(text = title)
        Text(text = description)
        Button(
            onClick = {
                //TODO:
            }
        ) {
            Text(text = buttonText)
        }
    }
}