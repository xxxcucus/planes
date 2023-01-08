package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.planes.android.preferences.SinglePlayerSettingsFragment
import com.planes.multiplayer_engine.responses.VersionResponse
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SinglePlayerSettingsFragmentTest {

    @Test
    fun testOnAttach() {
        var fragmentScenario = launchFragment<SinglePlayerSettingsFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.m_PreferencesService).isNotNull()
            Truth.assertThat(fragment.m_MainPreferencesService).isNotNull()
            Truth.assertThat(fragment.m_MultiplayerRound).isNotNull()
            Truth.assertThat(fragment.m_PlaneRound).isNotNull()
            Truth.assertThat(fragment.m_Context).isNotNull()
        }

        fragmentScenario.close()
    }

    @Test
    fun testFinalizeSavingError() {
        var fragmentScenario = launchFragment<SinglePlayerSettingsFragment>(
            initialState = Lifecycle.State.RESUMED
        )

        fragmentScenario.onFragment { fragment ->
            fragment.finalizeSavingError()
            Truth.assertThat(fragment.binding.settingsData!!.m_MultiplayerVersion).isFalse()
        }

        fragmentScenario.close()
    }

    @Test
    fun testFinalizeSavingSuccesfull() {
        var fragmentScenario = launchFragment<SinglePlayerSettingsFragment>(
            initialState = Lifecycle.State.RESUMED
        )

        fragmentScenario.onFragment { fragment ->
            fragment.finalizeSavingSuccessful()
            Truth.assertThat(fragment.m_MainPreferencesService.multiplayerVersion).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testCheckServerVersion() {
        var fragmentScenario = launchFragment<SinglePlayerSettingsFragment>(
            initialState = Lifecycle.State.RESUMED
        )

        fragmentScenario.onFragment { fragment ->
            var response = VersionResponse(m_VersionString = "testString")
            fragment.m_MainPreferencesService.serverVersion = "testString"
            Truth.assertThat(fragment.checkServerVersion(response)).isEmpty()
        }

        fragmentScenario.close()
    }
}