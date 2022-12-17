package denis.bayramgulov.hack4luck.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import denis.bayramgulov.hack4luck.model.AudioEncoding
import denis.bayramgulov.hack4luck.model.CommonModel.RUS_CODE

@JsonIgnoreProperties(ignoreUnknown = true)
data class SpecificationDto(
    val languageCode: String = RUS_CODE,
    val model: String = "general",
    val profanityFilter: Boolean = false,
    @JsonProperty("literature_text")
    val literatureText: Boolean = false,
    val audioEncoding: AudioEncoding = AudioEncoding.MP3,
    val sampleRateHertz: Int? = null,
    val audioChannelCount: Int? = null,
    val rawResults: Boolean = true
)
