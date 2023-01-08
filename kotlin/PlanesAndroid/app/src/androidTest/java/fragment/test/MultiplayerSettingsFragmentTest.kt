package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.planes.android.preferences.SinglePlayerSettingsFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MultiplayerSettingsFragmentTest {

    @Test
    fun testOnAttach() {
        var fragmentScenario = launchFragment<SinglePlayerSettingsFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.m_PreferencesService).isNotNull()
            Truth.assertThat(fragment.m_MainPreferencesService).isNotNull()
            Truth.assertThat(fragment.m_MultiplayerRound).isNotNull()
        }

        fragmentScenario.close()
    }
}