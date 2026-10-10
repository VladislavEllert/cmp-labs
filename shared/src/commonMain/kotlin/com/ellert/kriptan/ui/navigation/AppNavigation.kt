package com.ellert.kriptan.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ellert.kriptan.domain.CoinRepository
import com.ellert.kriptan.ui.detail.CoinDetailScreen
import com.ellert.kriptan.ui.list.CoinListScreen
import com.ellert.kriptan.ui.list.CoinListViewModel

@Composable
fun AppNavigation(
    backStack: SnapshotStateList<Route>,
    repository: CoinRepository,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
) {
    val listViewModel = viewModel { CoinListViewModel(repository) }
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
                    state = listViewModel.state,
                    isDarkTheme = isDarkTheme,
                    onSearchQueryChange = listViewModel::onSearchQueryChange,
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
