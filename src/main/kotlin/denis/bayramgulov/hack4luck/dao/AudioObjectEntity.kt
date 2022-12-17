package denis.bayramgulov.hack4luck.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AudioObjectEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AudioObjectEntity>(AudioObjectTable)

    var link by AudioObjectTable.link
    var path by AudioObjectTable.path
    var encoding by AudioObjectTable.encoding
    var rateHertz by AudioObjectTable.rateHertz
    var channelCount by AudioObjectTable.channelCount
    var status by AudioObjectTable.status
    var created by AudioObjectTable.created
    var operationId by AudioObjectTable.operationId
    var result by AudioObjectTable.result
}