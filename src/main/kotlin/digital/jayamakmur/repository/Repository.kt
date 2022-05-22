package digital.jayamakmur.repository

import digital.jayamakmur.model.Document


interface Repository<T : Document> {

    suspend fun get(id: String): T?

    suspend fun list(): List<T>

    suspend fun create(document: T): T

    suspend fun update(id: String, document: T): T

    suspend fun delete(id: String): Boolean
}