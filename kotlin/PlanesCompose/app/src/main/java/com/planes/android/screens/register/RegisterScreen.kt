package com.planes.android.screens.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.norobot.NoRobotViewModel
import com.planes.android.screens.norobot.PhotoModel
import com.planes.android.widgets.CommonTextFieldWithLogin
import com.planes.android.widgets.CommonTextFieldWithRegister
import com.planes.android.widgets.PasswordInputFieldWithLogin
import com.planes.android.widgets.PasswordInputFieldWithRegister

@Composable
fun RegisterScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                   navController: NavController,
                   registerViewModel: RegisterViewModel,
                   noRobotViewModel: NoRobotViewModel)  {

    currentScreenState.value = PlanesScreens.Register.name
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val submitClickedState = rememberSaveable {
        mutableStateOf(false)
    }

    //TODO: validation

    Column(modifier = modifier.fillMaxSize().verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(R.string.register),
            modifier = Modifier.padding(15.dp),
            style = MaterialTheme.typography.titleMedium)

        CommonTextFieldWithRegister(modifier = Modifier.padding(15.dp),
            registerViewModel,
            { register ->  register.getUserName()},
            { register, str -> register.setUserName(str)},
            onAction = KeyboardActions {
                keyboardController?.hide()
            },
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default,
            placeholder = stringResource(R.string.username)
        )

        val passwordVisibility = rememberSaveable {
            mutableStateOf(false)
        }

        PasswordInputFieldWithRegister(
            modifier = Modifier.padding(15.dp),
            registerViewModel,
            { register -> register.getPassword() },
            { register, str -> register.setPassword(str) },
            onAction = KeyboardActions {
                keyboardController?.hide()
            },
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default,
            placeholder = stringResource(R.string.password),
            passwordVisibility = passwordVisibility
        )

        Button(modifier = Modifier.padding(15.dp),
            onClick = {
                submitClickedState.value = true
                registerViewModel.register()
            }) {
            Text(text = stringResource(R.string.submit))
        }

        if (submitClickedState.value && registerViewModel.getLoading()) {
            Text(text = stringResource(R.string.loader_text))
        } else if (submitClickedState.value) {
            val error = registerViewModel.getError()

            if (error == null) {

                if (!registerViewModel.noRobotDataAvailable()) {
                    Toast.makeText(
                        LocalContext.current,
                        "No data available", //TODO
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    var imagesMapping = mapOf(
                        "2b36ea33-9c99-46dd-9f80-3bc648881c9b" to R.raw.image1,
                        "2e2379c7-cfcf-49a2-b47b-ed8d1a0c353a" to R.raw.image2,
                        "3debe3a5-cb77-4074-8759-908944b4ad5b" to R.raw.image3,
                        "4a8032e2-8fa0-41ee-8e02-4c40079a9dd8" to R.raw.image4,
                        "6c1c61b5-f752-45d3-9f3e-0f8184643b68" to R.raw.image5,
                        "7c315ae2-507b-4e51-96c7-e6f4b10597f9" to R.raw.image6,
                        "7ce5271d-c063-444a-82f2-068631f1f4a1" to R.raw.image7,
                        "7da77307-7e60-418d-b210-1845a1c657f3" to R.raw.image8,
                        "7f1ac6e1-cfc5-47d7-854f-bde3f3a5f53b" to R.raw.image9,
                        "8bd7d84a-45c8-4621-a2a7-750a4c6b80d4" to R.raw.image10,
                        "9f9a49c3-6c7c-4915-a206-9eb3603d6728" to R.raw.image11,
                        "31d94e7f-ecb8-4787-a579-21485e489fd7" to R.raw.image12,
                        "043adddf-90a7-4fde-88a1-a25e92c4532f" to R.raw.image13,
                        "65fd5538-e6eb-4970-8a7e-b2645df7c868" to R.raw.image14,
                        "085cf88e-6403-405d-8f9a-237861df3e49" to R.raw.image15,
                        "90f20a72-72f5-49a6-872b-6db44cd1368e" to R.raw.image16,
                        "517faf00-09a0-4f3d-ac77-c0397938d185" to R.raw.image17,
                        "06010c92-529a-4a72-8172-c56a7acd1622" to R.raw.image18,
                        "22635c95-5503-4c7b-ab32-2e678f935a43" to R.raw.image19,
                        "56969e76-ab31-47ca-864e-5052258d7bf2" to R.raw.image20,
                        "75823d2e-2591-4771-a9ae-ea14fa26ca58" to R.raw.image21,
                        "2264189b-c409-4ad3-9ede-872845dee031" to R.raw.image22,
                        "6530047e-8e6a-4cc6-8001-b55df824e8ef" to R.raw.image23,
                        "7269506c-ad95-4e63-934e-b1ae0c0b7a7e" to R.raw.image24,
                        "a1c3d89c-f15c-4876-a44d-3e85aa75e0fa" to R.raw.image25,
                        "a2b1982e-3928-4efe-b6a5-4ae447871e70" to R.raw.image26,
                        "a0198cf8-efb2-4c10-af74-e25745edebac" to R.raw.image27,
                        "b1f25a04-707a-4e73-92c4-effba955f6a0" to R.raw.image28,
                        "b4d8d197-e349-4667-88b1-604b245a7cf8" to R.raw.image29,
                        "baf9cdc5-dfe9-44ea-ba9a-0ba85d629fdc" to R.raw.image30,
                        "bbd38b59-ca0c-472c-a27a-ba69daeec260" to R.raw.image31,
                        "c02a5abb-61a6-4e5f-9f8d-633ef7849671" to R.raw.image32,
                        "c74dbcd5-f037-4c35-b6ff-8d5e28f83030" to R.raw.image33,
                        "d03ced07-e5b5-4a5a-89d3-1f54c26fa48f" to R.raw.image34,
                        "e923cf20-e4e7-4e48-ab4d-1c1e427645fa" to R.raw.image35,
                        "e8392d42-aa0c-4af3-89ea-37a563cb9214" to R.raw.image36,
                        "ebfc22ba-3117-49e2-9910-6b390610b2af" to R.raw.image37,
                        "fa093da7-546a-4627-ac80-6adcfd1431e9" to R.raw.image38
                    )

                    val noRobotImages = registerViewModel.getPhotoItems()
                    val noRobotEntries = noRobotImages.map {
                        PhotoModel(imagesMapping.get(it)!!, false)
                    }

                    noRobotViewModel.setImages(noRobotEntries)
                    noRobotViewModel.setQuestion(registerViewModel.getQuestion())
                    noRobotViewModel.setRequestId(registerViewModel.getPendingRequestId())

                    navController.navigate(PlanesScreens.NoRobot.name)
                }
            } else {
                Toast.makeText(
                    LocalContext.current,
                    registerViewModel.getError(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }
}