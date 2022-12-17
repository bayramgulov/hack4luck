package denis.bayramgulov.hack4luck.handler

import denis.bayramgulov.hack4luck.model.AudioEncoding
import denis.bayramgulov.hack4luck.model.OperationRequestResult
import denis.bayramgulov.hack4luck.model.OperationRequestResult.Error
import denis.bayramgulov.hack4luck.model.OperationRequestResult.NotFound
import denis.bayramgulov.hack4luck.model.OperationRequestResult.Success
import denis.bayramgulov.hack4luck.service.AudioService
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class AudioHandler(
    private val audioService: AudioService
) {

    suspend fun saveAudio(request: ServerRequest): ServerResponse =
        processServerRequestAndReturnValue {
            audioService.saveAudioObject(
                part = (request.awaitMultipartData()
                    .toSingleValueMap()
                    .getValue("audio") ?: error("Required body multipart data audio")) as FilePart,
                audioChannelCount = request.queryParamOrNull("channel_count")?.toIntOrNull(),
                audioEncoding = request.queryParamOrNull("encoding")?.let {
                    AudioEncoding.valueOf(it)
                } ?: AudioEncoding.MP3,
                audioRateHertz = request.queryParamOrNull("rate_hertz")?.toIntOrNull()
            )
        }

    suspend fun getRecognitionOperationStatus(request: ServerRequest): ServerResponse =
        processServerRequestAndReturnValue {
            audioService.getAudioObject(
                requestId = request.queryParamOrNull("request_id")?.toLong()
                    ?: error("Required parameter request_id is NULL")
            )
        }
}

suspend inline fun processServerRequestAndReturnValue(
    crossinline block: suspend () -> OperationRequestResult
): ServerResponse =
    when (val result: OperationRequestResult = block.invoke()) {
        is Success -> ServerResponse.ok()
            .bodyValueAndAwait(result.dto)

        is NotFound -> ServerResponse.notFound()
            .buildAndAwait()

        is Error -> ServerResponse.badRequest()
            .buildAndAwait()
    }