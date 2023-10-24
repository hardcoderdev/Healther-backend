package hardcoder.dev.healtherbackend.data.table

import org.jetbrains.exposed.sql.Table

object AdviceTable : Table(name = "advices") {

    val id = integer("id").autoIncrement().uniqueIndex()
    val title = varchar("title", 100)
    val contentText = varchar("contentText", 1000)
    val contentColorHex = varchar("contentColorHex", 20)
    val contentBackgroundColorHex = varchar("contentBackgroundColorHex", 20)
    val imagePath = varchar("path", 200)

    override val primaryKey = PrimaryKey(id)
}