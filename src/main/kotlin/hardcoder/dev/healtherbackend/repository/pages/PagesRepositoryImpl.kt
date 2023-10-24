package hardcoder.dev.healtherbackend.repository.pages

import hardcoder.dev.healtherbackend.data.DatabaseFactory.dbQuery
import hardcoder.dev.healtherbackend.data.entities.advices.Page
import hardcoder.dev.healtherbackend.data.table.PageTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.io.File

class PagesRepositoryImpl : PagesRepository {

    override suspend fun getAdvicePagesById(id: Int): List<Page> = dbQuery {
        PageTable.select { PageTable.adviceId eq id }
            .map(::resultRowToPage)
    }

    override suspend fun getPageById(id: Int): Page? = dbQuery {
        PageTable.select { PageTable.id eq id }
            .map(::resultRowToPage)
            .singleOrNull()
    }

    override suspend fun createPage(
        adviceId: Int,
        title: String,
        contentText: String,
        imagePath: String
    ): Page? = dbQuery {
        val insertStatement = PageTable.insert { pageTable ->
            pageTable.fillPageWithData(
                adviceId = adviceId,
                title = title,
                contentText = contentText,
                imagePath = imagePath
            )
        }

        insertStatement.resultedValues
            ?.singleOrNull()
            ?.let(::resultRowToPage)
    }

    override suspend fun updatePage(
        pageId: Int,
        adviceId: Int,
        title: String,
        contentText: String,
        imagePath: String?
    ): Boolean = dbQuery {
        val pageToUpdate = getPageById(pageId)
        val oldImage = File(requireNotNull(pageToUpdate?.imagePath))
        oldImage.delete()

        PageTable.update({ PageTable.adviceId eq adviceId }) { pageTable ->
            pageTable.fillPageWithData(
                adviceId = adviceId,
                title = title,
                contentText = contentText,
                imagePath = imagePath
            )
        } > 0
    }

    override suspend fun deletePageById(id: Int): Boolean = dbQuery {
        val pageToDelete = getPageById(id)
        pageToDelete?.imagePath?.let { File(it).delete() }

        PageTable.deleteWhere { PageTable.id eq id } > 0
    }

    override suspend fun deleteAllPagesByAdviceId(id: Int): Boolean = dbQuery {
        getAdvicePagesById(id).forEach { pageToDelete ->
            pageToDelete.imagePath.let { File(it).delete() }
        }

        PageTable.deleteWhere { adviceId eq id } > 0
    }

    private fun UpdateBuilder<Int>.fillPageWithData(
        adviceId: Int,
        title: String,
        contentText: String,
        imagePath: String?
    ) {
        this[PageTable.adviceId] = adviceId
        this[PageTable.title] = title
        this[PageTable.contentText] = contentText
        imagePath?.let {
            this[PageTable.imagePath] = it
        }
    }

    private fun resultRowToPage(row: ResultRow) = Page(
        id = row[PageTable.id],
        adviceId = row[PageTable.adviceId],
        title = row[PageTable.title],
        contentText = row[PageTable.contentText],
        imagePath = row[PageTable.imagePath]
    )
}