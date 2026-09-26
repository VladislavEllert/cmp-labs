package com.ellert.kriptan.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ellert.kriptan.domain.Coin
import com.ellert.kriptan.ui.common.ChangePill
import com.ellert.kriptan.ui.common.CoinAvatar
import com.ellert.kriptan.ui.format.formatCompact
import com.ellert.kriptan.ui.format.formatPrice
import com.ellert.kriptan.ui.theme.Dimens
import kriptan.shared.generated.resources.Res
import kriptan.shared.generated.resources.app_name
import kriptan.shared.generated.resources.header_change
import kriptan.shared.generated.resources.header_coin
import kriptan.shared.generated.resources.header_price
import kriptan.shared.generated.resources.ic_dark_mode
import kriptan.shared.generated.resources.ic_light_mode
import kriptan.shared.generated.resources.language_code
import kriptan.shared.generated.resources.toggle_theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    coins: List<Coin>,
    isDarkTheme: Boolean,
    onCoinClick: (String) -> Unit,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_name), fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onToggleLanguage) {
                        Text(stringResource(Res.string.language_code))
                    }
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            painter = painterResource(
                                if (isDarkTheme) Res.drawable.ic_light_mode else Res.drawable.ic_dark_mode
                            ),
                            contentDescription = stringResource(Res.string.toggle_theme),
                        )
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            item { ListHeader() }
            items(coins, key = { it.id }) { coin ->
                CoinRow(coin = coin, onClick = { onCoinClick(coin.id) })
            }
        }
    }
}

@Composable
private fun ListHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingSmall),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
    ) {
        val style = MaterialTheme.typography.labelMedium
        val color = MaterialTheme.colorScheme.onSurfaceVariant
        Text(stringResource(Res.string.header_coin), style = style, color = color, modifier = Modifier.weight(1f))
        Text(stringResource(Res.string.header_price), style = style, color = color)
        Text(
            text = stringResource(Res.string.header_change),
            style = style,
            color = color,
            textAlign = TextAlign.End,
            modifier = Modifier.width(Dimens.ChangePillWidth),
        )
    }
}

@Composable
private fun CoinRow(coin: Coin, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CoinAvatar(coin.symbol)
        Column(modifier = Modifier.weight(1f)) {
            Text(coin.symbol.uppercase(), fontWeight = FontWeight.Bold)
            Text(
                text = "${coin.name} | ${formatCompact(coin.totalVolume)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(formatPrice(coin.currentPrice), fontWeight = FontWeight.Bold)
        ChangePill(coin.priceChangePercentage24h, Modifier.width(Dimens.ChangePillWidth))
    }
}
