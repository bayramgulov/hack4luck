package denis.bayramgulov.hack4luck.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChunkDto(
    val alternatives: List<AlternativeDto>,
    val channelTag: Int
)
