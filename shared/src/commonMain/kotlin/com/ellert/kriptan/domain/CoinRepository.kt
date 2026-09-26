package com.ellert.kriptan.domain

interface CoinRepository {
    fun getCoins(): List<Coin>
    fun getCoin(id: String): Coin?
}
