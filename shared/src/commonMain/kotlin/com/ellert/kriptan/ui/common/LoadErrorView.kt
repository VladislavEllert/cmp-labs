package com.ellert.kriptan.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ellert.kriptan.domain.LoadError
import com.ellert.kriptan.ui.theme.Dimens
import kriptan.shared.generated.resources.Res
import kriptan.shared.generated.resources.coin_not_found
import kriptan.shared.generated.resources.error_bad_response
import kriptan.shared.generated.resources.error_no_connection
import kriptan.shared.generated.resources.error_server
import kriptan.shared.generated.resources.error_timeout
import kriptan.shared.generated.resources.error_too_many_requests
import kriptan.shared.generated.resources.retry
import org.jetbrains.compose.resources.stringResource

// No retry for "not found": asking again will not make the coin appear.
@Composable
fun LoadErrorView(error: LoadError, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    val message = when (error) {
        is LoadError.NoConnection -> stringResource(Res.string.error_no_connection)
        is LoadError.Timeout -> stringResource(Res.string.error_timeout)
        is LoadError.NotFound -> stringResource(Res.string.coin_not_found)
        is LoadError.TooManyRequests -> stringResource(Res.string.error_too_many_requests)
        is LoadError.Server -> stringResource(Res.string.error_server, error.code.toString())
        is LoadError.BadResponse -> stringResource(Res.string.error_bad_response)
    }
    Column(
        modifier = modifier.padding(Dimens.SpacingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
    ) {
        Text(message, textAlign = TextAlign.Center)
        if (error !is LoadError.NotFound) {
            FilledTonalButton(onClick = onRetry) { Text(stringResource(Res.string.retry)) }
        }
    }
}
