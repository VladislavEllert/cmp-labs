package com.ellert.kriptan.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// GET coins/{id}. Prices come as maps keyed by currency: {"usd": 75977, ...}.
@Serializable
data class CoinDetailDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: ImageDto,
    @SerialName("market_cap_rank") val marketCapRank: Int? = null,
    val description: Map<String, String> = emptyMap(),
    @SerialName("market_data") val marketData: MarketDataDto,
    @SerialName("last_updated") val lastUpdated: String,
)

@Serializable
data class ImageDto(val large: String)

@Serializable
data class MarketDataDto(
    @SerialName("current_price") val currentPrice: Map<String, Double>,
    @SerialName("market_cap") val marketCap: Map<String, Double>,
    @SerialName("total_volume") val totalVolume: Map<String, Double>,
    @SerialName("high_24h") val high24h: Map<String, Double> = emptyMap(),
    @SerialName("low_24h") val low24h: Map<String, Double> = emptyMap(),
    @SerialName("price_change_percentage_24h") val priceChangePercentage24h: Double? = null,
    @SerialName("circulating_supply") val circulatingSupply: Double? = null,
    @SerialName("total_supply") val totalSupply: Double? = null,
    @SerialName("max_supply") val maxSupply: Double? = null,
    val ath: Map<String, Double> = emptyMap(),
    @SerialName("ath_date") val athDate: Map<String, String> = emptyMap(),
)
