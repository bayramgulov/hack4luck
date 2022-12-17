package denis.bayramgulov.hack4luck.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import denis.bayramgulov.hack4luck.model.CommonModel
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecognitionResultDto(
    val done: Boolean,
    val id: String,
    val response: ResponseDto?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonModel.LOCAL_DATE_TIME_FORMAT)
    val createdAt: LocalDateTime,
    val createdBy: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonModel.LOCAL_DATE_TIME_FORMAT)
    val modifiedAt: LocalDateTime
)
