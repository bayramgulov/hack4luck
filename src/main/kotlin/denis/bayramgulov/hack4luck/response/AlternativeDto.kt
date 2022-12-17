package denis.bayramgulov.hack4luck.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AlternativeDto(
    val words: List<WordDto>,
    val text: String,
    val confidence: Int
)
