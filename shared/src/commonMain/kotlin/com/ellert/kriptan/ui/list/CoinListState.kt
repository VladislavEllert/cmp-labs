package com.ellert.kriptan.ui.list

import com.ellert.kriptan.domain.Coin
import com.ellert.kriptan.domain.LoadError

sealed interface CoinListState {
    data object Loading : CoinListState

    data class Error(val error: LoadError) : CoinListState

    // A failed next page is not a screen error: loaded coins stay, "Retry" is shown under them.
    data class Success(
        val searchQuery: String,
        val foundCoins: List<Coin>,
        val isLoadingMore: Boolean = false,
        val loadMoreError: LoadError? = null,
    ) : CoinListState
}
