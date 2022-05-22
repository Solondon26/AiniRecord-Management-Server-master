package digital.jayamakmur.service

import digital.jayamakmur.model.Document
import org.litote.kmongo.coroutine.CoroutineCollection

open class MongoDBRepository<T : Document>(protected val collection: CoroutineCollection<T>) {

    suspend fun get(id: String) = collection.findOneById(id)

    suspend fun list() = collection.find().toList()

    suspend fun create(document: T): T {
        collection.insertOne(document)
        return document
    }

    suspend fun update(id: String, document: T): T {
        collection.updateOneById(id, document)
        return document
    }

}