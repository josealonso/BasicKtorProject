package com.example.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import java.io.File
// import kotlinx.coroutines.Dispatchers


interface DAOFacade {
    suspend fun saveMessage(body: String)
    suspend fun getMessages(): List<String>
}

class DAOFacadeImpl : DAOFacade {
    override suspend fun saveMessage(body: String)  = dbQuery {
        Messages.insert { it[Messages.text] = body }   // Compile error
    }

    override suspend fun getMessages(): List<String> = dbQuery {
        Messages.selectAll().map { it[Messages.text] }   // Compile error
    }
}

fun initDatabase(config: ApplicationConfig) {
    val driverClassName = config.property("storage.driverClassName").getString()
    val jdbcURL = config.property("storage.jdbcURL").getString() +
            // used to provide an absolute path for H2 on-disk option:
            (config.propertyOrNull("storage.dbFilePath")?.getString()?.let {
                File(it).canonicalFile.absolutePath
            } ?: "")

    Database.connect(createHikariDataSource(   // Compile error
        url = jdbcURL,
        driver = driverClassName,
        user = "mysql",
        pwd = "mysql"
    ))

//    transaction {
//        SchemaUtils.create(Messages)
//    }
}

private fun createHikariDataSource(
    url: String,
    driver: String,
    user: String,
    pwd: String
) = HikariDataSource(HikariConfig().apply {
    driverClassName = driver
    jdbcUrl = url
    maximumPoolSize = 3
    isAutoCommit = false
    transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    username = user
    password = pwd
    validate()
})

// This code was written for using Exposed, not sqlDelight
//
// object Messages : Table() {
//    val id = integer("id").autoIncrement()
//    val text = varchar("body", 1024)
//
//    override val primaryKey = PrimaryKey(id)
//}
//
//private suspend fun <T> dbQuery(block: () -> T): T =
//    newSuspendedTransaction(Dispatchers.IO) { block() }
