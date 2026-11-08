package com.ellert.kriptan.data.network

import com.ellert.kriptan.data.dto.CoinDetailDto
import com.ellert.kriptan.data.dto.CoinMarketDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

// One method per server address. Paths have no leading "/": the base URL ends with one.
class CoinGeckoApi(private val client: HttpClient) {

    suspend fun getMarkets(page: Int, perPage: Int): List<CoinMarketDto> =
        client.get("coins/markets") {
            parameter("vs_currency", CURRENCY)
            parameter("order", "market_cap_desc")
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    suspend fun getCoin(id: String): CoinDetailDto =
        client.get("coins/$id") {
            parameter("localization", false)
            parameter("tickers", false)
            parameter("community_data", false)
            parameter("developer_data", false)
        }.body()

    companion object {
        const val CURRENCY = "usd"
    }
}
