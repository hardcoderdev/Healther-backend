package hardcoder.dev.healtherbackend.data

import hardcoder.dev.healtherbackend.data.table.AdviceTable
import hardcoder.dev.healtherbackend.data.table.PageTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(url = jdbcURL, driver = driverClassName)
        transaction(database) {
            SchemaUtils.create(
                AdviceTable,
                PageTable
            )
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(
        Dispatchers.IO
    ) {
        block()
    }
}