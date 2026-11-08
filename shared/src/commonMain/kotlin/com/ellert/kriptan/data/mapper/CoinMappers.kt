package com.ellert.kriptan.data.mapper

import com.ellert.kriptan.data.dto.CoinDetailDto
import com.ellert.kriptan.data.dto.CoinMarketDto
import com.ellert.kriptan.data.network.CoinGeckoApi.Companion.CURRENCY
import com.ellert.kriptan.domain.Coin
import com.ellert.kriptan.domain.CoinDetail

private val htmlTag = Regex("<[^>]*>")

fun CoinMarketDto.toDomain() = Coin(
    id = id,
    symbol = symbol,
    name = name,
    image = image,
    currentPrice = currentPrice,
    marketCap = marketCap,
    marketCapRank = marketCapRank,
    totalVolume = totalVolume,
    high24h = high24h,
    low24h = low24h,
    priceChangePercentage24h = priceChangePercentage24h,
    circulatingSupply = circulatingSupply,
    totalSupply = totalSupply,
    maxSupply = maxSupply,
    ath = ath,
    athDate = athDate,
    lastUpdated = lastUpdated,
)

fun CoinDetailDto.toDomain() = CoinDetail(
    coin = Coin(
        id = id,
        symbol = symbol,
        name = name,
        image = image.large,
        currentPrice = marketData.currentPrice[CURRENCY] ?: 0.0,
        marketCap = marketData.marketCap[CURRENCY]?.toLong() ?: 0L,
        marketCapRank = marketCapRank,
        totalVolume = marketData.totalVolume[CURRENCY] ?: 0.0,
        high24h = marketData.high24h[CURRENCY],
        low24h = marketData.low24h[CURRENCY],
        priceChangePercentage24h = marketData.priceChangePercentage24h,
        circulatingSupply = marketData.circulatingSupply,
        totalSupply = marketData.totalSupply,
        maxSupply = marketData.maxSupply,
        ath = marketData.ath[CURRENCY] ?: 0.0,
        athDate = marketData.athDate[CURRENCY] ?: lastUpdated,
        lastUpdated = lastUpdated,
    ),
    // The description is HTML with links; the screen shows plain text.
    description = description["en"]?.replace(htmlTag, "")?.trim()?.ifEmpty { null },
)
