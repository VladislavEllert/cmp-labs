package com.ellert.kriptan.ui.navigation

sealed interface Route {
    data object CoinList : Route
    data class CoinDetail(val coinId: String) : Route
}
