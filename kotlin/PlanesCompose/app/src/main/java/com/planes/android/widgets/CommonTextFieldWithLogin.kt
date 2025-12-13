package com.planes.android.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.planes.android.screens.login.LoginViewModel
import com.planes.android.screens.preferences.PreferencesViewModel

@Composable
fun CommonTextFieldWithLogin(modifier: Modifier,
                             loginViewModel: LoginViewModel,
                             valueReadLambda: @Composable (LoginViewModel)->String,
                             valueWriteLambda: (LoginViewModel, String)->Unit,
                             placeholder: String,
                             keyboardType: KeyboardType,
                             imeAction: ImeAction,
                             onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueReadLambda.invoke(loginViewModel),
        onValueChange = {
            valueWriteLambda.invoke(loginViewModel, it)
        } ,
        label = {
            Text(
                text = placeholder
            )
        },
        maxLines = 1,
        singleLine = true,
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
