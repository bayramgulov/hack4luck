package denis.bayramgulov.hack4luck.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResponseDto(
    val chunks: List<ChunkDto>?
)
