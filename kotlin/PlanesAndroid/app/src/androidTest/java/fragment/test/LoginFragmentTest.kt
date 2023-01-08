package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.planes.android.login.LoginFragment
import org.junit.Test
import org.junit.runner.RunWith
import com.planes.android.R

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @Test
    fun testOnAttach() {
        var fragmentScenario = launchFragment<LoginFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.m_PreferencesService).isNotNull()
            Truth.assertThat(fragment.m_MultiplayerRound).isNotNull()
        }

        fragmentScenario.close()
    }

    @Test
    fun testValidateUsernamePassword() {
        var fragmentScenario = launchFragment<LoginFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.validationUsernamePasswordLogin("", "testPassword").isNullOrEmpty()).isFalse()
            Truth.assertThat(fragment.validationUsernamePasswordLogin("testUsername", "").isNullOrEmpty()).isFalse()
            Truth.assertThat(fragment.validationUsernamePasswordLogin("testUsername", "testPasswordtestPasswordtestPassword").isNullOrEmpty()).isFalse()
            Truth.assertThat(fragment.validationUsernamePasswordLogin("testUsernametestUsernametestUsername", "testPassword").isNullOrEmpty()).isFalse()
            Truth.assertThat(fragment.validationUsernamePasswordLogin("testUsername", "testPassword").isNullOrEmpty()).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testFinalizeLoginSuccessfull() {
        var fragmentScenario = launchFragment<LoginFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            fragment.finalizeLoginSuccessful()
            Truth.assertThat(fragment.binding.creategame.isEnabled).isTrue()

        }

        fragmentScenario.close()
    }

    @Test
    fun testUseCredentialsFromPreferences() {
        var fragmentScenario = launchFragmentInContainer<LoginFragment>(
            initialState = Lifecycle.State.RESUMED
        )

        fragmentScenario.onFragment { fragment ->
            fragment.m_PreferencesService.password = "testPassword"
            fragment.m_PreferencesService.username = "testUsername"
        }
        onView(withId(R.id.credentials_preferences)).perform(click())

        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.binding.settingsData!!.m_Password == "testPassword").isTrue()
            Truth.assertThat(fragment.binding.settingsData!!.m_Username == "testUsername").isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testSaveCredentials() {
        var fragmentScenario = launchFragment<LoginFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            fragment.saveCredentials("testUsername", "testPassword", "testAuthToken")
            Truth.assertThat(fragment.m_MultiplayerRound.getUsername() == "testUsername").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getPassword() == "testPassword").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getAuthToken() == "testAuthToken").isTrue()
        }

        fragmentScenario.close()
    }
}