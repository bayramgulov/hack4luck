package denis.bayramgulov.hack4luck.settings

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object DatabaseSettings {
    val url: String = System.getenv("DB_URL")
    val user: String = System.getenv("DB_USER")
    val password: String = System.getenv("DB_PASSWORD")

    val db by lazy {
        Database.connect(hikari())
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = url
        config.username = user
        config.password = password
        config.maximumPoolSize = 10
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }
}