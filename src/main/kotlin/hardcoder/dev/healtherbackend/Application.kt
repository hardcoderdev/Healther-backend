package hardcoder.dev.healtherbackend

import hardcoder.dev.healtherbackend.data.DatabaseFactory
import hardcoder.dev.healtherbackend.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
    )
}

fun Application.module() {
    DatabaseFactory.init()

    configureDI()
    configureSerialization()
    configureHTTP()
    configureStatusPages()
    configureRouting()
    configureShutdownURL()
}
