package hardcoder.dev.healtherbackend.plugins

import hardcoder.dev.healtherbackend.constants.SHUTDOWN_URL
import io.ktor.server.application.*
import io.ktor.server.engine.*

fun Application.configureShutdownURL() {
    install(ShutDownUrl.ApplicationCallPlugin) {
        shutDownUrl = System.getenv(SHUTDOWN_URL)
        exitCodeSupplier = { 0 }
    }
}