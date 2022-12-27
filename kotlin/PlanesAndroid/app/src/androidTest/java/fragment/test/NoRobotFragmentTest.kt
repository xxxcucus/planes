package fragment.test

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.planes.android.register.NoRobotFragment
import com.planes.android.register.RegisterFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoRobotFragmentTest {
    @Test
    fun testOnAttach() {
        val bundle = bundleOf(
            Pair("norobot/requestid", "1"),
            Pair("norobot/images", arrayOf("2e2379c7-cfcf-49a2-b47b-ed8d1a0c353a", "4a8032e2-8fa0-41ee-8e02-4c40079a9dd8", "4a8032e2-8fa0-41ee-8e02-4c40079a9dd8",
                "6c1c61b5-f752-45d3-9f3e-0f8184643b68", "7c315ae2-507b-4e51-96c7-e6f4b10597f9", "7ce5271d-c063-444a-82f2-068631f1f4a1",
                 "7da77307-7e60-418d-b210-1845a1c657f3", "7f1ac6e1-cfc5-47d7-854f-bde3f3a5f53b", "7f1ac6e1-cfc5-47d7-854f-bde3f3a5f53b")),
            Pair("norobot/question", "dog"),
            Pair("norobot/selection", arrayOf(true, true, true, false, false, false, true, true, true)))

        var fragmentScenario = launchFragment<NoRobotFragment>(
            fragmentArgs = bundle,
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.m_RequestId == 1L).isTrue()
            Truth.assertThat(fragment.m_Images[8] == "7f1ac6e1-cfc5-47d7-854f-bde3f3a5f53b").isTrue()
            Truth.assertThat(fragment.m_Question == "dog").isTrue()
            Truth.assertThat(fragment.m_Selection[8]).isTrue()
        }

        fragmentScenario.close()
    }
}
