package com.example

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.Collections
import kotlin.time.Duration

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    val connections = Collections.synchronizedSet<MyConnection>(LinkedHashSet())

    routing {
        webSocket("/chat") {
            val c = MyConnection(this)
            connections += c

            c.session.send("Welcome to the chat. ${connections.size} users are online")
            for (frame in incoming) {
                // we are only interested in the text frames
                frame as? Frame.Text ?: continue
                val text = frame.readText()

                val message = "${c.name} $text"
                // send the message to everyone
                connections.forEach { it.session.send(message) }
            }
        }
    }

}