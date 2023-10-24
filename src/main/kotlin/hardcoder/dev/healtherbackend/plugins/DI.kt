package hardcoder.dev.healtherbackend.plugins

import hardcoder.dev.healtherbackend.di.dataModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureDI() {
    install(Koin) {
        modules(dataModule)
    }
}