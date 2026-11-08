package com.ellert.kriptan.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// One item of GET coins/markets.
@Serializable
data class CoinMarketDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    @SerialName("current_price") val currentPrice: Double,
    @SerialName("market_cap") val marketCap: Long,
    @SerialName("market_cap_rank") val marketCapRank: Int? = null,
    @SerialName("total_volume") val totalVolume: Double,
    @SerialName("high_24h") val high24h: Double? = null,
    @SerialName("low_24h") val low24h: Double? = null,
    @SerialName("price_change_percentage_24h") val priceChangePercentage24h: Double? = null,
    @SerialName("circulating_supply") val circulatingSupply: Double? = null,
    @SerialName("total_supply") val totalSupply: Double? = null,
    @SerialName("max_supply") val maxSupply: Double? = null,
    val ath: Double,
    @SerialName("ath_date") val athDate: String,
    @SerialName("last_updated") val lastUpdated: String,
)
