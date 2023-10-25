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
        val driverClassName = System.getenv("JDBC_DRIVER")
        val jdbcURL = System.getenv("DATABASE_URL")
        val database = Database.connect(
            url = jdbcURL,
            driver = driverClassName,
            user = System.getenv("PGUSER"),
            password = System.getenv("PGPASSWORD"),
        )

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