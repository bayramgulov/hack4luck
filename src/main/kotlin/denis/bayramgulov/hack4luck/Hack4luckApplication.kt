package denis.bayramgulov.hack4luck

import denis.bayramgulov.hack4luck.settings.DatabaseSettings
import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Hack4luckApplication

fun main(args: Array<String>) {
    // Migrate database schema structure
    val flyway: Flyway = Flyway.configure()
        .dataSource(DatabaseSettings.url, DatabaseSettings.user, DatabaseSettings.password)
        .load()
    flyway.migrate()
    // Run Spring Boot application
    runApplication<Hack4luckApplication>(*args)
}