package com.ellert.kriptan.ui.list

import com.ellert.kriptan.domain.Coin

data class CoinListState(
    val searchQuery: String = "",
    val foundCoins: List<Coin> = emptyList(),
)
