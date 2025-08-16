package com.planes.android.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.planes.android.preferences.PreferencesViewModel

@Composable
fun CheckBoxOption(text: String,
                   selected: Boolean,
                   onClickAction: () -> Unit) {
    Text(text = text,
        modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth(fraction = 0.75f).
        border(width = 4.dp, color = if (selected)  Color.Blue else Color.Transparent,
            shape = RoundedCornerShape(15.dp)
        ).clip(
            RoundedCornerShape(topStartPercent = 50, topEndPercent = 50,
            bottomEndPercent = 50, bottomStartPercent = 50)
        ).clickable {
            onClickAction.invoke()
        },
        textAlign = TextAlign.Center)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(modifier: Modifier,
                    optionsViewModel: PreferencesViewModel,
                    valueReadLambda: @Composable (PreferencesViewModel)->String,
                    valueWriteLambda: (PreferencesViewModel, String)->Unit,
                    placeholder: String,
                    keyboardType: KeyboardType,
                    imeAction: ImeAction,
                    onAction: KeyboardActions = KeyboardActions.Default
) {
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
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions.Default,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            cursorColor = Color.Black
        ),
        shape = RoundedCornerShape(15.dp),
        //modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp)
        modifier = modifier.fillMaxWidth(fraction = 0.75f)
    )
}
