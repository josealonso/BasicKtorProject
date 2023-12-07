package com.example

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val client = HttpClient {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket(port = 8080, path = "/chat") {
            val outgoingMessages = launch {
                while (true) {
                    val text = readln()
                    send(text)
                }
            }

            val incomingMessages = launch {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    println(frame.readText())
               }
            }

            outgoingMessages.join()
            incomingMessages.cancelAndJoin()
        }
    }
}