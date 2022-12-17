package denis.bayramgulov.hack4luck.client

import denis.bayramgulov.hack4luck.properties.YandexSpeechProperties
import denis.bayramgulov.hack4luck.request.AudioObjectDto
import denis.bayramgulov.hack4luck.response.RecognitionOperationDto
import denis.bayramgulov.hack4luck.response.RecognitionResultDto
import io.netty.handler.logging.LogLevel
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.netty.http.client.HttpClient
import reactor.netty.transport.logging.AdvancedByteBufFormat

@Component
class YandexSpeechClient(
    properties: YandexSpeechProperties
) {

    private val client: WebClient = WebClient.builder()
        .clientConnector(ReactorClientHttpConnector(httpClient()))
        .defaultHeader("Authorization", "Bearer ${properties.iam}")
        .exchangeStrategies(strategies())
        .build()

    suspend fun startRecognition(dto: AudioObjectDto): RecognitionOperationDto =
        this.client.post()
            .uri("https://transcribe.api.cloud.yandex.net/speech/stt/v2/longRunningRecognize")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .retrieve()
            .awaitBody()

    suspend fun getRecognition(operationId: String): RecognitionResultDto =
        this.client.get()
            .uri("https://operation.api.cloud.yandex.net/operations/{operationId}", operationId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBody()

    private fun httpClient(): HttpClient = HttpClient
        .create()
        .wiretap("reactor.netty.http.client.HttpClient",
            LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)

    private fun strategies(): ExchangeStrategies {
        val size = 16 * 1024 * 1024
        return ExchangeStrategies.builder()
            .codecs { codecs: ClientCodecConfigurer ->
                codecs.defaultCodecs().maxInMemorySize(size)
            }.build()
    }
}