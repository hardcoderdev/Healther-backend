package hardcoder.dev.healtherbackend.routing.advices.routes

import hardcoder.dev.healtherbackend.constants.ADVICES_IMAGES_PATH
import hardcoder.dev.healtherbackend.repository.pages.PagesRepository
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

fun Routing.pageRoutes() {
    val pagesRepository by inject<PagesRepository>()

    get<Advices.Id.Pages> { adviceIdPagesRoute ->
        val pages = pagesRepository.getAdvicePagesById(adviceIdPagesRoute.adviceId.id)
        if (pages.isNotEmpty()) {
            call.respond(
                status = HttpStatusCode.OK,
                message = pages
            )
        } else {
            call.respond(
                status = HttpStatusCode.NoContent,
                message = "Now the table pages in the database is empty."
            )
        }
    }

    get<Advices.Id.Pages.PageId> { pageIdRoute ->
        val page = pagesRepository.getPageById(pageIdRoute.pageId)
        page?.let {
            call.respond(
                status = HttpStatusCode.OK,
                message = page
            )
        } ?: run {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "No page was found for this ID."
            )
        }
    }

    post<Advices.Id.Pages> { createPageRoute ->
        val pageMultipartRequest = handleMultipart(ADVICES_IMAGES_PATH)
        pageMultipartRequest?.let {
            pagesRepository.createPage(
                adviceId = createPageRoute.adviceId.id,
                title = pageMultipartRequest.title,
                contentText = pageMultipartRequest.contentText,
                imagePath = requireNotNull(pageMultipartRequest.imageFileName)
            )?.let { createdPage ->
                call.respond(
                    status = HttpStatusCode.Created,
                    message = createdPage
                )
            } ?: run {
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = "The page hasn't been created, an error has occurred."
                )
            }
        }
    }

    put<Advices.Id.Pages.PageId.UpdatePage> { updatePageRoute ->
        val pageMultipartRequest = handleMultipart(ADVICES_IMAGES_PATH)
        pageMultipartRequest?.let {
            val isUpdated = pagesRepository.updatePage(
                pageId = updatePageRoute.pageId,
                adviceId = updatePageRoute.parent.parent.adviceId.id,
                title = pageMultipartRequest.title,
                contentText = pageMultipartRequest.contentText,
                imagePath = pageMultipartRequest.imageFileName
            )

            if (isUpdated) {
                call.respond(
                    status = HttpStatusCode.Created,
                    message = "The page has been successfully updated."
                )
            } else {
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = "The page hasn't been updated, an error has occurred."
                )
            }
        }
    }

    delete<Advices.Id.Pages.PageId.DeletePage> { deletePageRoute ->
        val isDeleted = pagesRepository.deletePageById(deletePageRoute.pageId)
        if (isDeleted) {
            call.respond(
                status = HttpStatusCode.OK,
                message = "The page was successfully deleted!"
            )
        } else {
            call.respond(
                status = HttpStatusCode.Conflict,
                message = "The page hasn't been deleted, an error has occurred."
            )
        }
    }

    delete<Advices.Id.Pages.DeleteAllPages> { deleteAllPagesRoute ->
        val isDeleted = pagesRepository.deleteAllPagesByAdviceId(deleteAllPagesRoute.parent.adviceId.id)
        if (isDeleted) {
            call.respond(
                status = HttpStatusCode.OK,
                message = "All advice pages was successfully deleted!"
            )
        } else {
            call.respond(
                status = HttpStatusCode.Conflict,
                message = "Advice pages haven't been deleted, an error has occurred."
            )
        }
    }
}