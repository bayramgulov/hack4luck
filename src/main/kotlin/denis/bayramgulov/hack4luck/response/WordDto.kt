package denis.bayramgulov.hack4luck.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class WordDto(
    val startTime: String,
    val endTime: String,
    val word: String,
    val confidence: Int
)
