package denis.bayramgulov.hack4luck.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("yandex-bucket")
class YandexBucketProperties {
    lateinit var keyId: String
    lateinit var secretKey: String
    lateinit var url: String
    lateinit var region: String
}