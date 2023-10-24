package hardcoder.dev.healtherbackend

import hardcoder.dev.healtherbackend.data.DatabaseFactory
import hardcoder.dev.healtherbackend.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init()

    configureDI()
    configureSerialization()
    configureHTTP()
    configureStatusPages()
    configureRouting()
    configureShutdownURL()
}
