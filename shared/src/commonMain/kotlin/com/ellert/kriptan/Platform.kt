package com.ellert.kriptan

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform