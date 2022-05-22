package digital.jayamakmur.model

import org.bson.codecs.pojo.annotations.BsonId

data class Product(
    @BsonId override val id: String,
    var name: String,
    var category: String,
    var price: Long,
    var stock: Long = 0,
    var discount: Int = 0,
    var cost: Long = 0,
    var rop: Long = 0,
    var sku: String,
    override val metadata: Metadata,
) : Document {

}