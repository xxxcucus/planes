package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.planes.android.login.LoginFragment
import org.junit.Test
import org.junit.runner.RunWith

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
    fun validateUsernamePassword() {
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
}