package denis.bayramgulov.hack4luck.model

import denis.bayramgulov.hack4luck.api.RecognitionOperationResponseDto

sealed class OperationRequestResult {
    object NotFound : OperationRequestResult()
    object Error : OperationRequestResult()
    data class Success(val dto: RecognitionOperationResponseDto) : OperationRequestResult()
}
