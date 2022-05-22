package digital.jayamakmur.model

import org.bson.codecs.pojo.annotations.BsonId

data class Invoice(
    @BsonId override val id: String,
    val order_id: String,
    val shipping_id: String,
    val customer: Person,
    val items: List<Item>,
    val tax_rate: Int,
    val tax: Long,
    val subtotal: Long,
    val shipping: Long,
    val discount: Long,
    val payment: List<Payment>,
    val total: Long,
    override val metadata: Metadata
) : Document {
    data class Item(val product: Product, val qty: Int)

}


data class Payment(val type: String, val value: Long)