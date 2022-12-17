package denis.bayramgulov.hack4luck.test

import denis.bayramgulov.hack4luck.model.CommonModel.LOCAL_DATE_TIME_FORMAT
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


internal class DataTimeFormatTest {

    @Test
    fun `must deserialize local date time`() {
        val formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)
        assertDoesNotThrow {
            val localDateTime = LocalDateTime.parse("2022-12-17T08:12:14Z", formatter)
            println(localDateTime.toString())
        }
    }
}