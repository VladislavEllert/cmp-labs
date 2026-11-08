package com.ellert.kriptan.data

import com.ellert.kriptan.data.mapper.toDomain
import com.ellert.kriptan.data.network.CoinGeckoApi
import com.ellert.kriptan.domain.Coin
import com.ellert.kriptan.domain.CoinDetail
import com.ellert.kriptan.domain.CoinRepository
import com.ellert.kriptan.domain.LoadError
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

class NetworkCoinRepository(private val api: CoinGeckoApi) : CoinRepository {

    override suspend fun getCoins(page: Int, perPage: Int): List<Coin> = request {
        api.getMarkets(page, perPage).map { it.toDomain() }
    }

    override suspend fun getCoinDetail(id: String): CoinDetail = request {
        api.getCoin(id).toDomain()
    }

    // JSON parsing and mapping run on Default, not on the UI thread.
    private suspend fun <T> request(block: suspend () -> T): T = withContext(Dispatchers.Default) {
        try {
            block()
        } catch (e: CancellationException) {
            // Cancellation is not an error: the screen is gone, nobody waits for the answer.
            throw e
        } catch (e: ResponseException) {
            throw when (e.response.status.value) {
                404 -> LoadError.NotFound()
                429 -> LoadError.TooManyRequests()
                else -> LoadError.Server(e.response.status.value)
            }
        } catch (e: HttpRequestTimeoutException) {
            throw LoadError.Timeout()
        } catch (e: SocketTimeoutException) {
            throw LoadError.Timeout()
        } catch (e: ContentConvertException) {
            throw LoadError.BadResponse()
        } catch (e: SerializationException) {
            throw LoadError.BadResponse()
        } catch (e: Exception) {
            // Engines report a missing network differently (UnresolvedAddressException,
            // ConnectException, NSURLError), so everything else counts as no connection.
            throw LoadError.NoConnection()
        }
    }
}
