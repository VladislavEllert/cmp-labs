package com.ellert.kriptan.ui.detail

import com.ellert.kriptan.domain.CoinDetail
import com.ellert.kriptan.domain.LoadError

sealed interface CoinDetailState {
    data object Loading : CoinDetailState

    data class Error(val error: LoadError) : CoinDetailState

    data class Success(val detail: CoinDetail) : CoinDetailState
}
