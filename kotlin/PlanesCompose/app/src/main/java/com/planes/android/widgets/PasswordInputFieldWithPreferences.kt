package com.planes.android.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.planes.android.preferences.PreferencesViewModel

@Composable
fun PasswordInputFieldWithPreferences(modifier: Modifier,
                                      optionsViewModel: PreferencesViewModel,
                                      valueReadLambda: @Composable (PreferencesViewModel)->String,
                                      valueWriteLambda: (PreferencesViewModel, String)->Unit,
                                      placeholder: String,
                                      passwordVisibility: MutableState<Boolean>,
                                      keyboardType: KeyboardType,
                                      imeAction: ImeAction,
                                      onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = valueReadLambda.invoke(optionsViewModel),
        onValueChange = {
            valueWriteLambda.invoke(optionsViewModel, it)
        } ,
        label = {
            Text(text = placeholder)
        },
        maxLines = 1,
        singleLine = true,
        visualTransformation = visualTransformation,
        trailingIcon = {
            PasswordVisibility(passwordVisibility = passwordVisibility)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions.Default,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,
            cursorColor = Color.Black
        ),
        shape = RoundedCornerShape(15.dp),
        //modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp)
        modifier = modifier.fillMaxWidth(fraction = 0.75f)
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton (onClick = {
        passwordVisibility.value = !visible
    }) {
        Icon(Icons.Default.Close,
           "Password visibility icon" )
    }
}
