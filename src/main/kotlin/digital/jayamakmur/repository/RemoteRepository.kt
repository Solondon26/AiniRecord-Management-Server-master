package digital.jayamakmur.repository

import digital.jayamakmur.model.Document
import digital.jayamakmur.model.Metadata
import org.litote.kmongo.coroutine.CoroutineCollection

class RemoteRepository<T : Document>(private val collection: CoroutineCollection<T>) : Repository<T> {
    override suspend fun get(id: String) = collection.findOneById(id)

    override suspend fun list() = collection.find().toList()

    override suspend fun create(document: T): T {
        collection.insertOne(document)
        return document
    }

    override suspend fun update(id: String, document: T): T {
        collection.updateOneById(id, document)
        return document
    }

    override suspend fun delete(id: String): Boolean {
        val document = get(id) ?: return false
        document.metadata.status = Metadata.MetadataStatus.DELETED
        update(id, document)
        return true
    }
}