package com.ellert.kriptan.ui.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

actual object LocalAppLocale {
    private val LocalLanguage = staticCompositionLocalOf { Locale.getDefault().language }

    @Composable
    actual infix fun provides(language: String): ProvidedValue<*> {
        Locale.setDefault(Locale.forLanguageTag(language))
        return LocalLanguage provides language
    }
}
