package denis.bayramgulov.hack4luck.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("yandex-speech")
class YandexSpeechProperties {
    lateinit var iam: String
}