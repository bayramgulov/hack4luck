package denis.bayramgulov.hack4luck.router

import denis.bayramgulov.hack4luck.handler.AudioHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class HackRouter(
    private val audioHandler: AudioHandler
) {

    @Bean
    fun router() = coRouter {
        "api".nest {
            POST("/audio", audioHandler::saveAudio)
            GET("/recognition.result", audioHandler::getRecognitionOperationStatus)
        }
    }
}