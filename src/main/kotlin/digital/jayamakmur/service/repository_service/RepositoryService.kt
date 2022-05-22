package digital.jayamakmur.service.repository_service

import digital.jayamakmur.model.Document
import digital.jayamakmur.repository.RepositoryUnit

abstract class RepositoryService<T : Document>(protected val repository: RepositoryUnit) {

    abstract suspend fun getOrder(id: String): T

    abstract suspend fun list(): List<T>
}

