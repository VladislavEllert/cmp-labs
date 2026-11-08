package com.ellert.kriptan.domain

// Why data could not be loaded. Repositories throw only these, so the UI never sees Ktor types.
sealed class LoadError : Exception() {
    class NoConnection : LoadError()
    class Timeout : LoadError()
    class NotFound : LoadError()

    // CoinGecko's free plan allows only a few requests per minute.
    class TooManyRequests : LoadError()
    class Server(val code: Int) : LoadError()
    class BadResponse : LoadError()
}
