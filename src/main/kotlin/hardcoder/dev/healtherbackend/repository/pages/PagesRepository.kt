package hardcoder.dev.healtherbackend.repository.pages

import hardcoder.dev.healtherbackend.data.entities.advices.Page

interface PagesRepository {
    suspend fun getAdvicePagesById(id: Int): List<Page>
    suspend fun getPageById(id: Int): Page?
    suspend fun createPage(
        adviceId: Int,
        title: String,
        contentText: String,
        imagePath: String
    ): Page?
    suspend fun updatePage(
        pageId: Int,
        adviceId: Int,
        title: String,
        contentText: String,
        imagePath: String?
    ): Boolean
    suspend fun deletePageById(id: Int): Boolean
    suspend fun deleteAllPagesByAdviceId(id: Int): Boolean
}