package com.ellert.kriptan.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ellert.kriptan.domain.Coin
import com.ellert.kriptan.domain.CoinRepository
import com.ellert.kriptan.domain.LoadError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext

class CoinListViewModel(private val repository: CoinRepository) : ViewModel() {
    private val _state = MutableStateFlow<CoinListState>(CoinListState.Loading)
    val state: StateFlow<CoinListState> = _state.asStateFlow()

    private var loadedCoins = emptyList<Coin>()
    private var loadedPages = 0
    private var hasMorePages = true
    private var searchQuery = ""

    // One page at a time: scrolling asks for "more" many times in a row.
    private val loadMoreMutex = Mutex()

    init {
        loadFirstPage()
    }

    fun onRetry() = loadFirstPage()

    fun onSearchQueryChange(query: String) {
        searchQuery = query
        _state.update { current ->
            if (current is CoinListState.Success) current.copy(searchQuery = query, foundCoins = filter()) else current
        }
    }

    fun onLoadMore() {
        // CoinGecko has no search in coins/markets, so search filters loaded coins and does not page.
        if (!hasMorePages || searchQuery.isNotBlank()) return
        if (!loadMoreMutex.tryLock()) return
        viewModelScope.launch {
            try {
                updateSuccess { it.copy(isLoadingMore = true, loadMoreError = null) }
                val page = repository.getCoins(page = loadedPages + 1, perPage = PAGE_SIZE)
                // Prices move between requests, so a coin can shift onto the next page twice.
                loadedCoins = withContext(Dispatchers.Default) { (loadedCoins + page).distinctBy { it.id } }
                // The page counter moves only after success: retry asks for the same page again.
                loadedPages++
                hasMorePages = page.size == PAGE_SIZE
                updateSuccess { it.copy(foundCoins = filter(), isLoadingMore = false) }
            } catch (e: LoadError) {
                updateSuccess { it.copy(isLoadingMore = false, loadMoreError = e) }
            } finally {
                loadMoreMutex.unlock()
            }
        }
    }

    private fun loadFirstPage() {
        _state.value = CoinListState.Loading
        viewModelScope.launch {
            try {
                val page = repository.getCoins(page = 1, perPage = PAGE_SIZE)
                loadedCoins = page
                loadedPages = 1
                hasMorePages = page.size == PAGE_SIZE
                _state.value = CoinListState.Success(searchQuery = searchQuery, foundCoins = filter())
            } catch (e: LoadError) {
                _state.value = CoinListState.Error(e)
            }
        }
    }

    private fun filter(): List<Coin> {
        val query = searchQuery.trim()
        if (query.isEmpty()) return loadedCoins
        return loadedCoins.filter {
            it.name.contains(query, ignoreCase = true) || it.symbol.contains(query, ignoreCase = true)
        }
    }

    private fun updateSuccess(transform: (CoinListState.Success) -> CoinListState.Success) {
        _state.update { if (it is CoinListState.Success) transform(it) else it }
    }

    private companion object {
        const val PAGE_SIZE = 50
    }
}
