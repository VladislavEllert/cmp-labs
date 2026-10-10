package com.ellert.kriptan.ui.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

actual object LocalAppLocale {
    @Composable
    actual infix fun provides(language: String): ProvidedValue<*> {
        val locale = Locale.forLanguageTag(language)
        Locale.setDefault(locale)
        val configuration = LocalConfiguration.current
        configuration.setLocale(locale)
        val resources = LocalContext.current.resources
        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return LocalConfiguration provides configuration
    }
}
