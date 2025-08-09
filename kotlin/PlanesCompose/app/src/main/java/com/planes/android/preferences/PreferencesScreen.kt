package com.planes.android.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens

@Composable
fun PreferencesScreen(modifier: Modifier,
                                  currentScreenState: MutableState<String>,
                                  navController: NavController,
                                  optionsViewModel: PreferencesViewModel = hiltViewModel()
) {

    currentScreenState.value = PlanesScreens.Preferences.name

    Column(modifier = modifier.fillMaxSize().padding(start = 15.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {


        val computerSkillsArray = stringArrayResource(R.array.computer_skills)

        Row(modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Start) {
            Text(text = stringResource(R.string.computer_skill))
            Column(modifier = Modifier.padding(start = 15.dp, top = 10.dp))  {
                Row(modifier = Modifier.padding(start = 10.dp)) {
                    RadioButton(selected = optionsViewModel.getComputerSkill() == 0,
                        onClick = {
                            optionsViewModel.setComputerSkill(0)
                    },
                        modifier = Modifier.size(10.dp))
                    Text(text = computerSkillsArray[0],
                        modifier = Modifier.padding(start = 15.dp))
                }
                Row(modifier = Modifier.padding(start = 10.dp)) {
                    RadioButton(selected = optionsViewModel.getComputerSkill() == 1,
                        onClick = {
                            optionsViewModel.setComputerSkill(1)
                    },
                        modifier = Modifier.size(10.dp))
                    Text(text = computerSkillsArray[1],
                        modifier = Modifier.padding(start = 15.dp))
                }
                Row(modifier = Modifier.padding(start = 10.dp)) {
                    RadioButton(selected = optionsViewModel.getComputerSkill() == 2,
                        onClick = {
                            optionsViewModel.setComputerSkill(2)
                    },
                        modifier = Modifier.size(10.dp))
                    Text(text = computerSkillsArray[2],
                        modifier = Modifier.padding(start = 15.dp))
                }
            }
        }

        Row(modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Start) {
            Text(text = stringResource(R.string.show_plane_after_kill))
            val yesNoOptionsArray = stringArrayResource(R.array.yesno_options)
            Column(modifier = Modifier.padding(start = 15.dp, top = 10.dp)) {
                Row(modifier = Modifier.padding(start = 10.dp)) {
                    RadioButton(selected = optionsViewModel.getShowPlaneAfterKill(),
                        onClick = {
                            optionsViewModel.setShowPlaneAfterKill(true)
                        },
                        modifier = Modifier.size(10.dp))
                    Text(text = yesNoOptionsArray[0],
                        modifier = Modifier.padding(start = 15.dp))
                }
                Row(modifier = Modifier.padding(start = 10.dp)) {
                    RadioButton(selected = !optionsViewModel.getShowPlaneAfterKill(),
                        onClick = {
                            optionsViewModel.setShowPlaneAfterKill(false)
                        },
                        modifier = Modifier.size(10.dp))
                    Text(text = yesNoOptionsArray[1],
                        modifier = Modifier.padding(start = 15.dp))
                }
            }
        }


        Row(modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Start) {
            Text(text = stringResource(R.string.username))
            TextField(value = optionsViewModel.getUserName(),
                onValueChange = {
                    optionsViewModel.setUserName(it)
                },
                modifier = Modifier.padding(start = 10.dp))
        }

        Row(modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Start) {
            Text(text = stringResource(R.string.password))
            TextField(value = optionsViewModel.getPassword(),
                onValueChange = {
                    optionsViewModel.setPassword(it)
                },
                modifier = Modifier.padding(start = 10.dp))
        }

    }
}