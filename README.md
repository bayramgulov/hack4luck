# Service for audio recognition with Yandex SpeechKit

## General information

Service created for Hackwagon22, it main purpose to recognize audio file using integration with Yandex AI API.

___

## Working with service examples

1. __Uploading audio file__:
```http request
POST http://localhost:8080/api/audio?channel_count=1&encoding=LINEAR16_PCM&rate_hertz=48000 HTTP/1.1
Content-Type: multipart/form-data; boundary=boundary
Accept: application/json

--boundary
Content-Disposition: form-data; name="audio"; filename="audio.wav"
```
Query params description:
* `encoding` - enum class with possible values _LINEAR16_PCM_ (WAV format), _OGG_OPUS_ (OGG), _MP3_, if not provided using _MP3_.
* `channel_count` - audio channel count, using only with WAV encoding files.
* `rate_hertz` - audio channel rate hertz, using only with WAV encoding files.

Response:
```json
{
    "requestId": 6,
    "status": "INIT",
    "text": null
}
```
Response parameter:
* `requestId` - id of asynchronous recognition operation
* `status` - current operation status
* `text` - recognized text from audio file

After successfully uploaded audio file request saving in database, and operation have _INIT_ status.

2. __Getting recognize operation status__:
```http request
GET http://localhost:8080/api/recognition.result?request_id=6
Accept: application/json
```
Responses:
```json
{
  "requestId": 6,
  "status": "UPLOADED",
  "text": null
}

```
After uploading saved locally file to Yandex Bucket operation changed status to _UPLOADED_.
```json
{
  "requestId": 6,
  "status": "PROCESSING",
  "text": null
}

```
After starting recognition uploaded Object in Yandex SpeechKit operation changed status to _PROCESSING_.
```json
{
  "requestId": 6,
  "status": "DONE",
  "text": "начало колесная пара восемьдесят семь шестьдесят двадцать девятый завод год десятый завод двадцать девятый шайба"
}
```
After recognition operation finished status changed to _DONE_ and result returning in `text` parameters
___

## Requirements

- PostgreSQL database with __DB_URL__, __DB_USER__, __DB_PASSWORD__ environment parameters
- Http port 8080 must be free
- File application.yml has Yandex Bucket and Yandex Speech configuration parameters

___

## Database configuration

### PostgreSQL
Set environment parameters with empty database, for example:
```bash
export DB_URL=jdbc:postgresql://localhost:5432/hack_4_luck
export DB_USER=postgres
export DB_PASSWORD=postgres
```

Or in _DatabaseSettings_ class variables can be hardcoded, like this:
```kotlin
    val url: String = "jdbc:postgresql://localhost:5432/hack_4_luck"
    val user: String = "postgres"
    val password: String = "postgres"
```

___

## Http port

In main configuration file _application.yml_ http port can be changed:
```yaml
server:
  port: 8080
```

___

## Yandex keys

In main configuration file _application.yml_ are section for SpeechKit and Bucket integration

### SpeechKit

For accessing Yandex SpeechKit integration [IAM token](https://cloud.yandex.com/en-ru/docs/iam/operations/iam-token/create-for-sa) needed.
Acquired token must be assigned for example:
```yaml
# ===============================
# = Yandex Speech
# ===============================
yandex-speech:
  iam: <IAM token>
```

### Bucket

For uploading audio files to Yandex Bucket [authorized key](https://cloud.yandex.com/en-ru/docs/iam/operations/authorized-key/create) needed.
Kei Id and key secret for AmazonS3 client must be configured, for example:
```yaml
# ===============================
# = Yandex Bucket
# ===============================
yandex-bucket:
  key-id: <key-id>
  secret-key: <secret-key>
  url: storage.yandexcloud.net
  region: ru-central1
```
