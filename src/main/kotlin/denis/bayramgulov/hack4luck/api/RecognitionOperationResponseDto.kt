package denis.bayramgulov.hack4luck.api

import denis.bayramgulov.hack4luck.model.RecognitionStatus

data class RecognitionOperationResponseDto(
    val requestId: Long,
    val status: RecognitionStatus,
    val text: String? = null
)