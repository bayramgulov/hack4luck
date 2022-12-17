package denis.bayramgulov.hack4luck.job

import denis.bayramgulov.hack4luck.service.AudioService
import denis.bayramgulov.hack4luck.service.RecognitionService
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RecognitionJob(
    private val audioService: AudioService,
    private val recognitionService: RecognitionService
) {

    @Scheduled(initialDelay = 5000, fixedRate = 5000)
    fun uploadStoreObject() {
        runBlocking {
            audioService.uploadAudioObject()
        }
    }

    @Scheduled(initialDelay = 5000, fixedRate = 5000)
    fun startRecognitionOperation() {
        runBlocking {
            recognitionService.startRecognitionProcess()
        }
    }

    @Scheduled(initialDelay = 5000, fixedRate = 10000)
    fun checkRecognitionOperation() {
        runBlocking {
            recognitionService.checkRecognitionProcess()
        }
    }
}