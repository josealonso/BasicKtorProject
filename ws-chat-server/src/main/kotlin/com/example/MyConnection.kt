package com.example

import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class MyConnection(val session: DefaultWebSocketSession) {
    companion object {
        var lastId = AtomicInteger(0)
    }
    // generate a name for the user
    val name = "user${lastId.getAndIncrement()}"
}