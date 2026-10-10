package com.ellert.kriptan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ellert.kriptan.data.MockCoinRepository
import com.ellert.kriptan.ui.locale.LocalAppLocale
import com.ellert.kriptan.ui.navigation.AppNavigation
import com.ellert.kriptan.ui.navigation.Route
import com.ellert.kriptan.ui.theme.KriptanTheme

@Composable
fun App() {
    val repository = remember { MockCoinRepository() }
    val backStack = remember { mutableStateListOf<Route>(Route.CoinList) }
    var isDarkTheme by remember { mutableStateOf(true) }
    var language by remember { mutableStateOf("ru") }

    CompositionLocalProvider(LocalAppLocale provides language) {
        // Resources read the locale once per composition, so a new language needs a fresh subtree.
        key(language) {
            KriptanTheme(darkTheme = isDarkTheme) {
                AppNavigation(
                    backStack = backStack,
                    repository = repository,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme },
                    onToggleLanguage = { language = if (language == "ru") "en" else "ru" },
                )
            }
        }
    }
}
