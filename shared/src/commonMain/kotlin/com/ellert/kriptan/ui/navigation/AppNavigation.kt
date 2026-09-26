package com.ellert.kriptan.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ellert.kriptan.domain.CoinRepository
import com.ellert.kriptan.ui.detail.CoinDetailScreen
import com.ellert.kriptan.ui.list.CoinListScreen

@Composable
fun AppNavigation(
    backStack: SnapshotStateList<Route>,
    repository: CoinRepository,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
) {
    val coins = remember(repository) { repository.getCoins() }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = {
            slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width / 3 }
        },
        popTransitionSpec = {
            slideInHorizontally { width -> -width / 3 } togetherWith slideOutHorizontally { width -> width }
        },
        predictivePopTransitionSpec = {
            slideInHorizontally { width -> -width / 3 } togetherWith slideOutHorizontally { width -> width }
        },
        entryProvider = entryProvider {
            entry<Route.CoinList> {
                CoinListScreen(
                    coins = coins,
                    isDarkTheme = isDarkTheme,
                    onCoinClick = { coinId -> backStack.add(Route.CoinDetail(coinId)) },
                    onToggleTheme = onToggleTheme,
                    onToggleLanguage = onToggleLanguage,
                )
            }
            entry<Route.CoinDetail> { route ->
                CoinDetailScreen(
                    coin = repository.getCoin(route.coinId),
                    onBack = { backStack.removeLastOrNull() },
                )
            }
        },
    )
}
