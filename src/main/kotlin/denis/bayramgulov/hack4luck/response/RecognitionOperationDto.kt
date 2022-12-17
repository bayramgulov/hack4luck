package denis.bayramgulov.hack4luck.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import denis.bayramgulov.hack4luck.model.CommonModel.LOCAL_DATE_TIME_FORMAT
import java.time.LocalDateTime
import java.time.ZonedDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecognitionOperationDto(
    val done: Boolean,
    val id: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_TIME_FORMAT)
    val createdAt: LocalDateTime,
    val createdBy: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_TIME_FORMAT)
    val modifiedAt: LocalDateTime
)
