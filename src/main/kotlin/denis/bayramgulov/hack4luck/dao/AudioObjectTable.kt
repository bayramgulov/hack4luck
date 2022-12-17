package denis.bayramgulov.hack4luck.dao

import denis.bayramgulov.hack4luck.model.AudioEncoding
import denis.bayramgulov.hack4luck.model.RecognitionStatus
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object AudioObjectTable : LongIdTable("audio_object") {
    val link: Column<String?> = varchar("public_link", 512).nullable()
    val path: Column<String> = varchar("path", 512)
    val encoding: Column<AudioEncoding> = enumerationByName("audio_encoding", 16)
    val rateHertz: Column<Int?> = integer("rate_hertz").nullable()
    val channelCount: Column<Int?> = integer("channel_count").nullable()
    val status: Column<RecognitionStatus> = enumerationByName("recognition_status", 32)
    val created: Column<LocalDateTime> = datetime("created").index("idx_audio_object_created")
    val operationId: Column<String?> = varchar("operation_id", 255)
        .uniqueIndex("uk_audio_object_operation_id")
        .nullable()
    val result: Column<String?> = text("recognition_text").nullable()
}