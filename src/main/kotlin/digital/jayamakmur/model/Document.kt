package digital.jayamakmur.model

import java.util.*

interface Document {
    val id: String
    val metadata: Metadata
}

data class Metadata(val created: Long, var status: MetadataStatus) {

    enum class MetadataStatus {
        ACTIVE, DELETED
    }

    companion object {
        fun create() = Metadata(Calendar.getInstance().timeInMillis, MetadataStatus.ACTIVE)
    }
}
