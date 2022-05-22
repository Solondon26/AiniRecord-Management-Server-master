package digital.jayamakmur.model

import org.bson.codecs.pojo.annotations.BsonId

data class StockOutReceipt(
    @BsonId override val id: String,
    override val product: String,
    override val stock: Long,
    override val value: Long,
    val desc: String, override val metadata: Metadata,
) : StockReceipt {
    override val flow: StockReceipt.StockFlow = StockReceipt.StockFlow.OUT
}
