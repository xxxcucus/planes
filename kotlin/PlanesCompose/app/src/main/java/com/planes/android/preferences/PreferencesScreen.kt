package com.planes.android.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.widgets.CheckBoxOption
import com.planes.android.widgets.CommonTextFieldWithPreferencesViewModel
import com.planes.android.widgets.PasswordInputFieldWithPreferencesViewModel
import com.planes.singleplayerengine.PlanesRoundInterface

@Composable
fun PreferencesScreen(modifier: Modifier,
                      currentScreenState: MutableState<String>,
                      navController: NavController,
                      optionsViewModel: PreferencesViewModel,
                      planeRound: PlanesRoundInterface
) {

    currentScreenState.value = PlanesScreens.Preferences.name
    val scrollState = rememberScrollState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.fillMaxSize().padding(start = 15.dp).
    verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally) {

        val computerSkillsArray = stringArrayResource(R.array.computer_skills)

        Column(modifier = Modifier.padding(start = 15.dp, top = 10.dp))  {

            Text(text = stringResource(R.string.computer_skill),
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally))

            CheckBoxOption(text = computerSkillsArray[0],
                optionsViewModel.getComputerSkill() == 0,
                        {
                            optionsViewModel.setComputerSkill(0)
                            planeRound.setComputerSkill(0)
                        })
            CheckBoxOption(text = computerSkillsArray[1],
                optionsViewModel.getComputerSkill() == 1,
                        {
                            optionsViewModel.setComputerSkill(1)
                            planeRound.setComputerSkill(1)
                        })
            CheckBoxOption(text = computerSkillsArray[2],
                optionsViewModel.getComputerSkill() == 2,
                        {
                            optionsViewModel.setComputerSkill(2)
                            planeRound.setComputerSkill(2)
                        })

        }


        val yesNoOptionsArray = stringArrayResource(R.array.yesno_options)
        Column(modifier = Modifier.padding(start = 15.dp, top = 10.dp)) {

            Text(text = stringResource(R.string.show_plane_after_kill),
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally))

            CheckBoxOption(text = yesNoOptionsArray[0],
                optionsViewModel.getShowPlaneAfterKill(),
                {
                    optionsViewModel.setShowPlaneAfterKill(true)
                    planeRound.setShowPlaneAfterKill(true)
                })
            CheckBoxOption(text = yesNoOptionsArray[1],
                !optionsViewModel.getShowPlaneAfterKill(),
                {
                    optionsViewModel.setShowPlaneAfterKill(false)
                    planeRound.setShowPlaneAfterKill(false)
                })

        }


        CommonTextFieldWithPreferencesViewModel(modifier = Modifier.padding(15.dp),
            optionsViewModel,
            { prefs ->  prefs.getUserName()},
            { prefs, str -> prefs.setUserName(str)},
            onAction = KeyboardActions {
                keyboardController?.hide()
            },
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default,
            placeholder = stringResource(R.string.username))

        

        val passwordVisibility = rememberSaveable {
            mutableStateOf(false)
        }
        PasswordInputFieldWithPreferencesViewModel(
            modifier = Modifier.padding(15.dp),
            optionsViewModel,
            { prefs -> prefs.getPassword() },
            { prefs, str -> prefs.setPassword(str) },
            onAction = KeyboardActions {
                keyboardController?.hide()
            },
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default,
            placeholder = stringResource(R.string.password),
            passwordVisibility = passwordVisibility
        )

    }
}


