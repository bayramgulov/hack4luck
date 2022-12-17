package denis.bayramgulov.hack4luck.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import denis.bayramgulov.hack4luck.properties.YandexBucketProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(YandexBucketProperties::class)
class YandexBucketConfig(
    private val properties: YandexBucketProperties
) {

    @Bean
    fun amazonS3(): AmazonS3 = AmazonS3ClientBuilder.standard()
        .withCredentials(AWSStaticCredentialsProvider(credentials()))
        .withEndpointConfiguration(
            AwsClientBuilder.EndpointConfiguration(
                properties.url, properties.region
            )
        )
        .build()

    private fun credentials(): AWSCredentials {
        return BasicAWSCredentials(
            properties.keyId,
            properties.secretKey
        )
    }
}