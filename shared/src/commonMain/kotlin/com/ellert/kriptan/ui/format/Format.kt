package com.ellert.kriptan.ui.format

import kotlin.math.abs
import kotlin.math.roundToLong

private const val NO_VALUE = "—"

fun formatPrice(value: Double?): String {
    if (value == null) return NO_VALUE
    val decimals = when {
        value >= 1 -> 2
        value >= 0.01 -> 4
        else -> 6
    }
    return "$" + formatFixed(value, decimals)
}

fun formatPercent(value: Double?): String {
    if (value == null) return NO_VALUE
    val sign = if (value >= 0) "+" else ""
    return sign + formatFixed(value, 2) + "%"
}

fun formatCompact(value: Double?): String {
    if (value == null) return NO_VALUE
    val (divider, suffix) = when {
        value >= 1e12 -> 1e12 to "T"
        value >= 1e9 -> 1e9 to "B"
        value >= 1e6 -> 1e6 to "M"
        else -> 1.0 to ""
    }
    return formatFixed(value / divider, 2) + suffix
}

fun formatDate(isoDateTime: String): String = isoDateTime.take(10)

fun formatDateTime(isoDateTime: String): String =
    isoDateTime.take(16).replace('T', ' ') + " UTC"

private fun formatFixed(value: Double, decimals: Int): String {
    var factor = 1L
    repeat(decimals) { factor *= 10 }
    val scaled = (abs(value) * factor).roundToLong()
    val sign = if (value < 0 && scaled != 0L) "-" else ""
    val whole = groupThousands(scaled / factor)
    val fraction = (scaled % factor).toString().padStart(decimals, '0')
    return "$sign$whole.$fraction"
}

private fun groupThousands(number: Long): String =
    number.toString().reversed().chunked(3).joinToString(",").reversed()
