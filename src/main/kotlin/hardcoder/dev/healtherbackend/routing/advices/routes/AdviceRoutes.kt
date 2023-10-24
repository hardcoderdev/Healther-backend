package hardcoder.dev.healtherbackend.routing.advices.routes

import hardcoder.dev.healtherbackend.constants.ADVICES_IMAGES_PATH
import hardcoder.dev.healtherbackend.repository.advices.AdvicesRepository
import hardcoder.dev.healtherbackend.routing.advices.Advices
import hardcoder.dev.healtherbackend.routing.advices.requests.handleMultipart
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Routing.adviceRoutes() {
    val advicesRepository by inject<AdvicesRepository>()

    get<Advices> {
        val limitCount = call.parameters["limit_count"]
        limitCount?.let {
            val allAdvices = advicesRepository.getAllAdvices().take(it.toInt())
            if (allAdvices.isNotEmpty()) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = allAdvices
                )
            } else {
                call.respond(
                    status = HttpStatusCode.NoContent,
                    message = "Now the table advices in the database is empty."
                )
            }
        } ?: run {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Specify the limit of the requested advices."
            )
        }
    }

    get<Advices.Id> {
        val advice = advicesRepository.getAdviceById(it.id)
        advice?.let {
            call.respond(status = HttpStatusCode.OK, message = advice)
        } ?: run {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "No advice was found for this ID."
            )
        }
    }

    delete<Advices.Id.DeleteAdvice> { deleteAdvice ->
        val isDeleted = advicesRepository.deleteAdviceById(deleteAdvice.adviceId.id)
        if (isDeleted) {
            call.respond(
                status = HttpStatusCode.OK,
                message = "The advice was successfully deleted!"
            )
        } else {
            call.respond(
                status = HttpStatusCode.Conflict,
                message = "The advice hasn't been deleted, an error has occurred."
            )
        }
    }

    put<Advices.Id.UpdateAdvice> { updateAdvice ->
        val adviceMultipartRequest = handleMultipart(ADVICES_IMAGES_PATH)
        adviceMultipartRequest?.let {
            val isUpdated = advicesRepository.updateAdvice(
                id = updateAdvice.adviceId.id,
                title = adviceMultipartRequest.title,
                contentText = adviceMultipartRequest.contentText,
                contentColorHex = adviceMultipartRequest.contentColorHex,
                contentBackgroundColorHex = adviceMultipartRequest.contentBackgroundColorHex,
                imagePath = adviceMultipartRequest.imageFileName
            )

            if (isUpdated) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = "The advice has been successfully updated."
                )
            } else {
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = "The advice hasn't been updated, an error has occurred."
                )
            }
        }
    }

    post<Advices> {
        val adviceMultipartRequest = handleMultipart(ADVICES_IMAGES_PATH)
        adviceMultipartRequest?.let {
            advicesRepository.createAdvice(
                title = adviceMultipartRequest.title,
                contentText = adviceMultipartRequest.contentText,
                contentColorHex = adviceMultipartRequest.contentColorHex,
                contentBackgroundColorHex = adviceMultipartRequest.contentBackgroundColorHex,
                imagePath = requireNotNull(adviceMultipartRequest.imageFileName)
            )?.let { createdAdvice ->
                call.respond(
                    status = HttpStatusCode.Created,
                    message = createdAdvice
                )
            } ?: run {
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = "The advice hasn't been created, an error has occurred."
                )
            }
        }
    }

    get<Advices.Images.ImageFileName> {
        val file = File("images/advices/${it.name}")
        if (file.exists()) {
            call.respondFile(file)
        }
        else call.respond(HttpStatusCode.NotFound)
    }
}


