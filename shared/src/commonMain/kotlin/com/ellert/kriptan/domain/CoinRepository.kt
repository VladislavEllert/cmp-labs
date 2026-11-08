package com.ellert.kriptan.domain

// Both methods throw LoadError when data cannot be loaded.
interface CoinRepository {
    // Pages start at 1, sorted by market cap. An empty page means the end of the list.
    suspend fun getCoins(page: Int, perPage: Int): List<Coin>

    suspend fun getCoinDetail(id: String): CoinDetail
}
