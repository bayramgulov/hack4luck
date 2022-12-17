package denis.bayramgulov.hack4luck.client

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectResult
import denis.bayramgulov.hack4luck.model.CommonModel.BUCKET_NAME
import denis.bayramgulov.hack4luck.model.CommonModel.STORAGE_BASE_URL
import mu.KLogging
import org.springframework.stereotype.Component
import java.io.File
import java.util.UUID

@Component
class YandexBucketClient(
    private val amazonS3Client: AmazonS3
) {

    companion object: KLogging()

    fun uploadObject(file: File): String {
        val fileKey = UUID.randomUUID().toString()
        val response: PutObjectResult = amazonS3Client.putObject(BUCKET_NAME, fileKey, file)
        logger.info { "Uploaded file to Yandex Bucket ${response.eTag}" }
        return "$STORAGE_BASE_URL/$BUCKET_NAME/$fileKey"
    }
}