package com.planes.android.screens.about

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.planes.android.R

class AboutEntryRepository {
    companion object AboutEntryRepository {
        fun create(version: String?, context: Context): List<AboutEntryModel> {
            var versionSection = AboutEntryModel("Version", "Software version is unknown", false, "Empty", "Empty")

            if (version != null)
                versionSection = AboutEntryModel(getString(context, R.string.software_version_title),
                    getString(context, R.string.software_version) + " " + version, false, "Empty", "Empty")
            val softwareSection = AboutEntryModel(getString(context, R.string.credits_software_title),
                getString(context, R.string.credits_software_content), true, getString(context, R.string.credits_software_button),
                "https://www.github.com/xxxcucus/planes")
            val graphicsSection = AboutEntryModel(getString(context, R.string.credits_graphics_title),
                getString(context, R.string.credits_graphics_content1), true, getString(context, R.string.credits_graphics_button),
                "https://axa951.wixsite.com/portfolio")

            val otherContributionText = """
                ${getString(context, R.string.credits_othercontributions_1)}
                ${getString(context, R.string.credits_othercontributions_2)}
                ${getString(context, R.string.credits_othercontributions_3)}
                """.trimIndent()
            val othersSection = AboutEntryModel(getString(context, R.string.credits_othercontributions_title),
                otherContributionText, false, "Empty", "Empty")
            val websiteSection = AboutEntryModel(getString(context, R.string.credits_website_title), getString(context, R.string.credits_website), true,
                getString(context, R.string.credits_website_button), "https://xxxcucus.github.io/planes/")

            return listOf(versionSection, softwareSection, graphicsSection, othersSection, websiteSection)
        }
    }
}