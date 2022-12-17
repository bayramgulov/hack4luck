package denis.bayramgulov.hack4luck.service

import denis.bayramgulov.hack4luck.api.RecognitionOperationResponseDto
import denis.bayramgulov.hack4luck.client.YandexBucketClient
import denis.bayramgulov.hack4luck.dao.AudioObjectEntity
import denis.bayramgulov.hack4luck.dao.AudioObjectTable
import denis.bayramgulov.hack4luck.model.AudioEncoding
import denis.bayramgulov.hack4luck.model.OperationRequestResult
import denis.bayramgulov.hack4luck.model.RecognitionStatus
import denis.bayramgulov.hack4luck.settings.DatabaseSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.reactor.awaitSingleOrNull
import mu.KLogging
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.Part
import org.springframework.stereotype.Service
import reactor.kotlin.adapter.rxjava.toFlowable
import java.io.File
import java.time.LocalDateTime
import java.util.*

@Service
class AudioService(
    private val yandexBucketClient: YandexBucketClient
) {

    companion object : KLogging()

    suspend fun saveAudioObject(
        part: FilePart,
        audioEncoding: AudioEncoding,
        audioRateHertz: Int?,
        audioChannelCount: Int?
    ): OperationRequestResult {
        val fileName = when (audioEncoding) {
            AudioEncoding.LINEAR16_PCM -> "${UUID.randomUUID()}.wav"
            AudioEncoding.OGG_OPUS -> "${UUID.randomUUID()}.ogg"
            AudioEncoding.MP3 -> "${UUID.randomUUID()}.mp3"
        }
        return try {
            val file = File(fileName)
            part.transferTo(file).awaitSingleOrNull()
            val requestId = newSuspendedTransaction(context = Dispatchers.IO, db = DatabaseSettings.db) {
                val entity = AudioObjectEntity.new {
                    link = null
                    path = file.absolutePath
                    encoding = audioEncoding
                    rateHertz = audioRateHertz
                    channelCount = audioChannelCount
                    status = RecognitionStatus.INIT
                    created = LocalDateTime.now()
                    operationId = null
                    result = null
                }
                entity.id.value
            }
            logger.info { "Saved new audio file with id $requestId" }
            OperationRequestResult.Success(
                dto = RecognitionOperationResponseDto(
                    requestId = requestId,
                    status = RecognitionStatus.INIT
                )
            )
        } catch (ex: Exception) {
            logger.error(ex) { "Error saving new audio object" }
            OperationRequestResult.Error
        }
    }

    suspend fun uploadAudioObject() {
        newSuspendedTransaction(context = Dispatchers.IO, db = DatabaseSettings.db) {
            AudioObjectEntity.find {
                AudioObjectTable.status eq RecognitionStatus.INIT
            }.forEach { entity ->
                val file = File(entity.path)
                try {
                    val objectLink: String = yandexBucketClient.uploadObject(file = file)
                    entity.status = RecognitionStatus.UPLOADED
                    entity.link = objectLink
                    logger.info { "Uploaded file to Yandex Bucket with link: $objectLink" }
                } catch (ex: Exception) {
                    logger.error(ex) { "Error uploading file to Yandex Bucket" }
                    entity.status = RecognitionStatus.ERROR
                } finally {
                    file.delete()
                }
            }
        }
    }

    suspend fun getAudioObject(requestId: Long): OperationRequestResult =
        try {
            newSuspendedTransaction(context = Dispatchers.IO, db = DatabaseSettings.db) {
                val entity = AudioObjectEntity.find {
                    AudioObjectTable.id eq requestId
                }.takeIf {
                    it.count() == 1L
                }?.first()
                if (entity != null) {
                    OperationRequestResult.Success(
                        dto = RecognitionOperationResponseDto(
                            requestId = requestId,
                            status = entity.status,
                            text = entity.result
                        )
                    )
                } else {
                    OperationRequestResult.NotFound
                }
            }
        } catch (ex: Exception) {
            logger.error(ex) { "Error fetching audio result" }
            OperationRequestResult.Error
        }
}