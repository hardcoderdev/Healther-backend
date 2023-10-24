package hardcoder.dev.healtherbackend.plugins

import hardcoder.dev.healtherbackend.routing.advices.routes.adviceRoutes
import hardcoder.dev.healtherbackend.routing.advices.routes.pageRoutes
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.resources.*
import java.io.File

fun Application.configureRouting() {
    install(Resources)
    routing {
        static("/") {
            staticRootFolder = File("images")
            files(".")
        }

        adviceRoutes()
        pageRoutes()
    }
}

