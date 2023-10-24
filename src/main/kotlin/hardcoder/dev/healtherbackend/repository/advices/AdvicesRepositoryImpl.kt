package hardcoder.dev.healtherbackend.repository.advices

import hardcoder.dev.healtherbackend.data.DatabaseFactory.dbQuery
import hardcoder.dev.healtherbackend.data.entities.advices.Advice
import hardcoder.dev.healtherbackend.data.table.AdviceTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.io.File

class AdvicesRepositoryImpl : AdvicesRepository {

    override suspend fun getAllAdvices(): List<Advice> = dbQuery {
        AdviceTable.selectAll().map(::resultRowToAdvice)
    }

    override suspend fun getAdviceById(id: Int): Advice? = dbQuery {
        AdviceTable
            .select { AdviceTable.id eq id }
            .map(::resultRowToAdvice)
            .singleOrNull()
    }

    override suspend fun createAdvice(
        title: String,
        contentText: String,
        contentColorHex: String,
        contentBackgroundColorHex: String,
        imagePath: String
    ): Advice? = dbQuery {
        val insertStatement = AdviceTable.insert { adviceTable ->
            adviceTable[AdviceTable.title] = title
            adviceTable[AdviceTable.contentText] = contentText
            adviceTable[AdviceTable.contentColorHex] = contentColorHex
            adviceTable[AdviceTable.contentBackgroundColorHex] = contentBackgroundColorHex
            adviceTable[AdviceTable.imagePath] = imagePath
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAdvice)
    }

    override suspend fun updateAdvice(
        id: Int,
        title: String,
        contentText: String,
        contentColorHex: String,
        contentBackgroundColorHex: String,
        imagePath: String?
    ): Boolean = dbQuery {
        val adviceToUpdate = getAdviceById(id)
        adviceToUpdate?.imagePath?.let { File(it).delete() }

        AdviceTable.update({ AdviceTable.id eq id }) { adviceTable ->
            adviceTable.fillWithData(
                title = title,
                contentText = contentText,
                contentColorHex = contentColorHex,
                contentBackgroundColorHex = contentBackgroundColorHex,
                imagePath = imagePath
            )
        } > 0
    }

    override suspend fun deleteAdviceById(id: Int): Boolean = dbQuery {
        val adviceToDelete = getAdviceById(id)
        adviceToDelete?.imagePath?.let { File(it).delete() }

        AdviceTable.deleteWhere { AdviceTable.id eq id } > 0
    }

    private fun resultRowToAdvice(row: ResultRow) = Advice(
        id = row[AdviceTable.id],
        title = row[AdviceTable.title],
        contentText = row[AdviceTable.contentText],
        contentColorHex = row[AdviceTable.contentColorHex],
        contentBackgroundColorHex = row[AdviceTable.contentBackgroundColorHex],
        imagePath = row[AdviceTable.imagePath]
    )

    private fun UpdateBuilder<Int>.fillWithData(
        title: String,
        contentText: String,
        contentColorHex: String,
        contentBackgroundColorHex: String,
        imagePath: String?
    ) {
        this[AdviceTable.title] = title
        this[AdviceTable.contentText] = contentText
        this[AdviceTable.contentColorHex] = contentColorHex
        this[AdviceTable.contentBackgroundColorHex] = contentBackgroundColorHex
        imagePath?.let {
            this[AdviceTable.imagePath] = it
        }
    }
}