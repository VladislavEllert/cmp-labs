package com.ellert.kriptan.data.network

import io.ktor.client.engine.HttpClientEngine

// Ktor has one API on every target, but the engine under it is platform-specific.
expect fun httpEngine(): HttpClientEngine
