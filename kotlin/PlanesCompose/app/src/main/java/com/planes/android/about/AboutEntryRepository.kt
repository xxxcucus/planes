package com.planes.android.about

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString
import com.planes.android.MainActivity
import com.planes.android.R

class AboutEntryRepository {
    companion object AboutEntryRepository {
        fun create(version: String?, context: Context): List<AboutEntryModel> {
            var version_section = AboutEntryModel("Version", "Software version is unknown", false, "Empty", "Empty")

            if (version != null)
                version_section = AboutEntryModel(getString(context, R.string.software_version_title),
                    getString(context, R.string.software_version) + " " + version, false, "Empty", "Empty")
            val software_section = AboutEntryModel(getString(context, R.string.credits_software_title),
                getString(context, R.string.credits_software_content), true, getString(context, R.string.credits_software_button),
                "https://www.github.com/xxxcucus/planes")
            val graphics_section = AboutEntryModel(getString(context, R.string.credits_graphics_title),
                getString(context, R.string.credits_graphics_content1), true, getString(context, R.string.credits_graphics_button),
                "https://axa951.wixsite.com/portfolio")

            val otherContributionText = """
                ${getString(context, R.string.credits_othercontributions_1)}
                ${getString(context, R.string.credits_othercontributions_2)}
                ${getString(context, R.string.credits_othercontributions_3)}
                """.trimIndent()
            val others_section = AboutEntryModel(getString(context, R.string.credits_othercontributions_title),
                otherContributionText, false, "Empty", "Empty")
            val website_section = AboutEntryModel(getString(context, R.string.credits_website_title), getString(context, R.string.credits_website), true,
                getString(context, R.string.credits_website_button), "https://xxxcucus.github.io/planes/")
            val tools_section = AboutEntryModel(getString(context, R.string.credits_tools_title),
                getString(context, R.string.credits_tools), false, "Empty", "Empty")

            return listOf(version_section, software_section, graphics_section, others_section, website_section)
        }
    }
}