package com.ellert.kriptan.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ellert.kriptan.ui.format.formatPercent
import com.ellert.kriptan.ui.theme.Dimens
import com.ellert.kriptan.ui.theme.OnPriceChange
import com.ellert.kriptan.ui.theme.PriceDown
import com.ellert.kriptan.ui.theme.PriceUp

@Composable
fun ChangePill(percent: Double?, modifier: Modifier = Modifier) {
    val color = when {
        percent == null -> MaterialTheme.colorScheme.surfaceVariant
        percent >= 0 -> PriceUp
        else -> PriceDown
    }
    Text(
        text = formatPercent(percent),
        color = OnPriceChange,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .background(color, RoundedCornerShape(Dimens.SpacingSmall))
            .padding(vertical = Dimens.SpacingSmall),
    )
}
