package digital.jayamakmur.model

import digital.jayamakmur.graph.Shipping
import org.bson.codecs.pojo.annotations.BsonId

data class Order(
    @BsonId override val id: String,
    val customer: Person,
    val shipping: Shipping,
    val items: List<OrderItem>,
    var status: Status,
    override val metadata: Metadata
) : Document {
    data class OrderItem(val product: String, val qty: Int)

    enum class Status { ACTIVE, CANCELED, PROCESSED }
}

