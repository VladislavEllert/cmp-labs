package com.ellert.kriptan.ui.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue

// Compose Resources pick strings by the platform locale, so each platform overrides it its own way.
expect object LocalAppLocale {
    @Composable
    infix fun provides(language: String): ProvidedValue<*>
}
