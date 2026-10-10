package com.ellert.kriptan.ui.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSArgumentDomain
import platform.Foundation.NSUserDefaults

actual object LocalAppLocale {
    private val LocalLanguage = staticCompositionLocalOf { "" }

    // The argument domain lives only in memory, so the chosen language is not saved to disk.
    @Composable
    actual infix fun provides(language: String): ProvidedValue<*> {
        NSUserDefaults.standardUserDefaults.setVolatileDomain(
            mapOf("AppleLanguages" to listOf(language)),
            NSArgumentDomain,
        )
        return LocalLanguage provides language
    }
}
