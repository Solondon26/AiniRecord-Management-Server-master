package digital.jayamakmur.repository

import digital.jayamakmur.model.Document


class MemoryRepository<T : Document> : Repository<T> {
    private val database = arrayListOf<T>()

    override suspend fun get(id: String) = database.find { it.id == id }

    override suspend fun list() = database.toList()

    override suspend fun create(document: T) = document.apply { database.add(this) }

    override suspend fun update(id: String, document: T): T {
        val index = database.indexOf(get(id))
        database[index] = document
        return database[index]
    }

    override suspend fun delete(id: String): Boolean = database.remove(get(id))

}
