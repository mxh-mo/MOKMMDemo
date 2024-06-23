package com.example.mokmmdemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform