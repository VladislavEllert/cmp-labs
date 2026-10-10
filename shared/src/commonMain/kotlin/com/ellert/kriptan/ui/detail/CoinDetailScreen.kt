package com.ellert.kriptan.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ellert.kriptan.domain.Coin
import com.ellert.kriptan.ui.common.ChangePill
import com.ellert.kriptan.ui.common.CoinAvatar
import com.ellert.kriptan.ui.format.formatCompact
import com.ellert.kriptan.ui.format.formatDate
import com.ellert.kriptan.ui.format.formatDateTime
import com.ellert.kriptan.ui.format.formatPrice
import com.ellert.kriptan.ui.theme.Dimens
import kriptan.shared.generated.resources.Res
import kriptan.shared.generated.resources.all_time_high
import kriptan.shared.generated.resources.back
import kriptan.shared.generated.resources.circulating_supply
import kriptan.shared.generated.resources.coin_not_found
import kriptan.shared.generated.resources.high_24h
import kriptan.shared.generated.resources.ic_arrow_back
import kriptan.shared.generated.resources.last_updated
import kriptan.shared.generated.resources.low_24h
import kriptan.shared.generated.resources.market_cap
import kriptan.shared.generated.resources.max_supply
import kriptan.shared.generated.resources.rank
import kriptan.shared.generated.resources.section_market
import kriptan.shared.generated.resources.section_supply
import kriptan.shared.generated.resources.total_supply
import kriptan.shared.generated.resources.volume_24h
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(coin: Coin?, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(coin?.name.orEmpty(), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(Res.drawable.ic_arrow_back), stringResource(Res.string.back))
                    }
                },
            )
        },
    ) { padding ->
        if (coin == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(stringResource(Res.string.coin_not_found))
            }
        } else {
            CoinDetails(coin, Modifier.padding(padding))
        }
    }
}

@Composable
private fun CoinDetails(coin: Coin, modifier: Modifier = Modifier) {
    val symbol = coin.symbol.uppercase()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.SpacingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
        ) {
            CoinAvatar(coin.symbol)
            Column {
                Text(symbol, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                coin.marketCapRank?.let { rank ->
                    Text(
                        text = stringResource(Res.string.rank, rank.toString()),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = formatPrice(coin.currentPrice),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            ChangePill(coin.priceChangePercentage24h, Modifier.width(Dimens.ChangePillWidth))
        }
        InfoSection(Res.string.section_market) {
            InfoRow(Res.string.market_cap, "$" + formatCompact(coin.marketCap.toDouble()))
            InfoRow(Res.string.volume_24h, "$" + formatCompact(coin.totalVolume))
            InfoRow(Res.string.high_24h, formatPrice(coin.high24h))
            InfoRow(Res.string.low_24h, formatPrice(coin.low24h))
            InfoRow(Res.string.all_time_high, "${formatPrice(coin.ath)} · ${formatDate(coin.athDate)}")
        }
        InfoSection(Res.string.section_supply) {
            InfoRow(Res.string.circulating_supply, "${formatCompact(coin.circulatingSupply)} $symbol")
            InfoRow(Res.string.total_supply, "${formatCompact(coin.totalSupply)} $symbol")
            InfoRow(Res.string.max_supply, "${formatCompact(coin.maxSupply)} $symbol")
        }
        Text(
            text = stringResource(Res.string.last_updated, formatDateTime(coin.lastUpdated)),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun InfoSection(title: StringResource, content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.SpacingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
        ) {
            Text(stringResource(title), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
private fun InfoRow(label: StringResource, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(label),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(value, fontWeight = FontWeight.Medium)
    }
}
