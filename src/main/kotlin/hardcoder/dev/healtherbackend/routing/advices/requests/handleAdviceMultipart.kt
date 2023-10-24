package hardcoder.dev.healtherbackend.routing.advices.requests

import hardcoder.dev.healtherbackend.extensions.save
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handleMultipart(
    pathToSave: String
): AdviceMultipartRequest? {
    val pageMultipartData = call.receiveMultipart()
    var imageFileName: String? = null
    var title = ""
    var contentText = ""
    var contentColorHex = ""
    var contentBackgroundColorHex = ""

    return try {
        pageMultipartData.forEachPart { partData ->
            when (partData) {
                is PartData.BinaryChannelItem -> Unit
                is PartData.BinaryItem -> Unit
                is PartData.FileItem -> {
                    imageFileName = partData.save(pathToSave)
                }

                is PartData.FormItem -> {
                    when (partData.name) {
                        "title" -> {
                            title = partData.value
                        }
                        "content_text" -> {
                            contentText = partData.value
                        }
                        "content_color_hex" -> {
                            contentColorHex = partData.value
                        }
                        "content_background_color_hex" -> {
                            contentBackgroundColorHex = partData.value
                        }
                    }
                }
            }
            partData.dispose()
        }

        AdviceMultipartRequest(
            title = title,
            contentText = contentText,
            contentColorHex = contentColorHex,
            contentBackgroundColorHex = contentBackgroundColorHex,
            imageFileName = imageFileName
        )
    } catch (e: Exception) {
        call.respond(
            status = HttpStatusCode.Conflict,
            message = "An error occurred while handling multipart request: ${e.localizedMessage}"
        )
        null
    }
}