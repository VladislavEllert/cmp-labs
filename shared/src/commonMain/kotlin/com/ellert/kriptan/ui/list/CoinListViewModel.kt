package com.ellert.kriptan.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ellert.kriptan.domain.CoinRepository

class CoinListViewModel(repository: CoinRepository) : ViewModel() {
    private val allCoins = repository.getCoins()

    var state by mutableStateOf(CoinListState(foundCoins = allCoins))
        private set

    fun onSearchQueryChange(searchQuery: String) {
        val query = searchQuery.trim()
        state = CoinListState(
            searchQuery = searchQuery,
            foundCoins = allCoins.filter {
                it.name.contains(query, ignoreCase = true) || it.symbol.contains(query, ignoreCase = true)
            },
        )
    }
}
