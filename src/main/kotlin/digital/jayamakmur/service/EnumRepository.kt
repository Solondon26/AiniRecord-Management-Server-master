package digital.jayamakmur.service

import digital.jayamakmur.model.Document
import digital.jayamakmur.model.Metadata
import org.bson.codecs.pojo.annotations.BsonId
import org.koin.java.KoinJavaComponent.inject
import org.litote.kmongo.coroutine.CoroutineCollection
import java.text.SimpleDateFormat
import java.util.*

class EnumService(private val ref: EnumCode) {
    private val repository by inject<EnumRepository>(EnumRepository::class.java)

    private fun getDate() = SimpleDateFormat("yyyy.MM").format(Calendar.getInstance().time)

    private suspend fun getNewEnum(document: EnumCode): String {
        val ref = repository.get(document.name) ?: repository.create(DocumentEnum(document.name, 0))
        ref.value++
        repository.update(ref.id, ref)
        return ref.value.toString().padStart(6, '0')
    }

    suspend fun getId() = "${ref.code}.${getDate()}.${getNewEnum(ref)}"
}

enum class EnumCode(val code: String) {
    Invoice("INV"),
    Order("PSM"),
    Product("EAN"),
    ShippingReceipt("SHR"),
    StockReceipt("STR"),
    LedgerLog("JU"),
}

class EnumRepository(collection: CoroutineCollection<DocumentEnum>) : MongoDBRepository<DocumentEnum>(collection) {


}

data class DocumentEnum(@BsonId override val id: String, var value: Long, override val metadata: Metadata = Metadata.create()) : Document