package hardcoder.dev

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import hardcoder.dev.plugins.configureHTTP
import hardcoder.dev.plugins.configureRouting
import hardcoder.dev.plugins.configureSecurity
import hardcoder.dev.plugins.configureSerialization

fun main() {
    embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(
        wait = true
    )
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
