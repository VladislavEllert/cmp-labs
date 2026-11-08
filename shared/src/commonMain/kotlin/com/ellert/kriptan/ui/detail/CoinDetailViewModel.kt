package com.ellert.kriptan.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ellert.kriptan.domain.CoinRepository
import com.ellert.kriptan.domain.LoadError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Lives as long as its back stack entry: "back" clears it, and viewModelScope cancels the request.
class CoinDetailViewModel(
    private val coinId: String,
    private val repository: CoinRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<CoinDetailState>(CoinDetailState.Loading)
    val state: StateFlow<CoinDetailState> = _state.asStateFlow()

    init {
        load()
    }

    fun onRetry() = load()

    private fun load() {
        _state.value = CoinDetailState.Loading
        viewModelScope.launch {
            _state.value = try {
                CoinDetailState.Success(repository.getCoinDetail(coinId))
            } catch (e: LoadError) {
                CoinDetailState.Error(e)
            }
        }
    }
}
