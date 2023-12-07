package com.example

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*

fun main() {
    val client = HttpClient {
        install(WebSockets)
    }

    client.webSocket(port = 8080, path = "/chat") {

    }
}