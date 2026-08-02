package com.planes.android.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.planes.android.navigation.PlanesScreens

@Composable
fun TextPopupWithButton(title: String, description: String, buttonText: String,
                        videoId: Int, navController: NavHostController) {
    Column( modifier = Modifier.wrapContentHeight().
    fillMaxWidth().padding(15.dp)) {
        Text(text = title,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(15.dp),
            style = MaterialTheme.typography.titleMedium)
        Text(text = description,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(15.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = {
                val time = 0
                navController.navigate(route = "${PlanesScreens.Tutorials.name}/${videoId}/${time}")
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = buttonText)
        }
    }
}