package hardcoder.dev.healtherbackend.data.table

import org.jetbrains.exposed.sql.Table

object PageTable : Table(name = "advice_pages") {

    val id = integer("id").autoIncrement().uniqueIndex()
    val adviceId = reference("adviceId", AdviceTable.id)
    val title = varchar("title", 100)
    val contentText = varchar("contentText", 1000)
    val imagePath = varchar("path", 200)

    override val primaryKey = PrimaryKey(id)
}