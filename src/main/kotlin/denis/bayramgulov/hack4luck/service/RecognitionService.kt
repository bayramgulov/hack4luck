package denis.bayramgulov.hack4luck.service

import denis.bayramgulov.hack4luck.client.YandexSpeechClient
import denis.bayramgulov.hack4luck.dao.AudioObjectEntity
import denis.bayramgulov.hack4luck.dao.AudioObjectTable
import denis.bayramgulov.hack4luck.model.RecognitionStatus
import denis.bayramgulov.hack4luck.request.AudioDto
import denis.bayramgulov.hack4luck.request.AudioObjectDto
import denis.bayramgulov.hack4luck.request.ConfigurationDto
import denis.bayramgulov.hack4luck.request.SpecificationDto
import denis.bayramgulov.hack4luck.settings.DatabaseSettings
import kotlinx.coroutines.Dispatchers
import mu.KLogging
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.stereotype.Service

@Service
class RecognitionService(
    private val client: YandexSpeechClient
) {

    companion object: KLogging()

    suspend fun startRecognitionProcess() {
        newSuspendedTransaction(context = Dispatchers.IO, db = DatabaseSettings.db) {
            AudioObjectEntity.find {
                AudioObjectTable.status eq RecognitionStatus.UPLOADED
            }.forEach { entity ->
                val operation = client.startRecognition(
                    dto = AudioObjectDto(
                        config = ConfigurationDto(
                            specification = SpecificationDto(
                                audioEncoding = entity.encoding,
                                sampleRateHertz = entity.rateHertz,
                                audioChannelCount = entity.channelCount
                            )
                        ),
                        audio = AudioDto(
                            uri = entity.link ?: error("link must not be null")
                        )
                    )
                )
                logger.info { "Yandex Speech recognition operation started: $operation" }
                entity.operationId = operation.id
                entity.status = RecognitionStatus.PROCESSING
            }
        }
    }

    suspend fun checkRecognitionProcess() {
        newSuspendedTransaction(context = Dispatchers.IO, db = DatabaseSettings.db) {
            AudioObjectEntity.find {
                AudioObjectTable.status eq RecognitionStatus.PROCESSING
            }.filter { entity ->
                entity.operationId != null
            }.forEach { entity ->
                val results = client.getRecognition(operationId = entity.operationId
                    ?: error("operationId must not be null"))
                if (results.done) {
                    logger.info { "Operation ${entity.operationId} is Done with result: $results" }
                    entity.result = results.response?.chunks?.flatMap { chunk ->
                        chunk.alternatives.map { it.text }
                    }?.joinToString(separator = " ")
                    entity.status = RecognitionStatus.DONE
                } else {
                    logger.info { "Operation ${entity.operationId} is In progress" }
                }
            }
        }
    }
}