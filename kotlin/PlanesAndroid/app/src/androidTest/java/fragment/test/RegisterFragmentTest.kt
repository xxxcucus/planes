package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.planes.android.register.RegisterFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterFragmentTest {

    @Test
    fun testOnAttach() {
        var fragmentScenario = launchFragment<RegisterFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.m_MultiplayerRound).isNotNull()
        }

        fragmentScenario.close()
    }

    @Test
    fun validateUsernamePassword() {
        var fragmentScenario = launchFragment<RegisterFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.validationUsernamePasswordRegister("", "testPassword").isNullOrEmpty()).isFalse()
            Truth.assertThat(fragment.validationUsernamePasswordRegister("testUsername", "").isNullOrEmpty()).isFalse()
            Truth.assertThat(fragment.validationUsernamePasswordRegister("testUsername", "testPasswordtestPasswordtestPassword").isNullOrEmpty()).isFalse()
            Truth.assertThat(fragment.validationUsernamePasswordRegister("testUsernametestUsernametestUsername", "testPassword").isNullOrEmpty()).isFalse()
            Truth.assertThat(fragment.validationUsernamePasswordRegister("testUsername", "testPassword").isNullOrEmpty()).isTrue()
            Truth.assertThat(fragment.validationUsernamePasswordRegister("testUsername", "test").isNullOrEmpty()).isFalse()
            Truth.assertThat(fragment.validationUsernamePasswordRegister("test", "testPassword").isNullOrEmpty()).isFalse()
        }

        fragmentScenario.close()
    }
}