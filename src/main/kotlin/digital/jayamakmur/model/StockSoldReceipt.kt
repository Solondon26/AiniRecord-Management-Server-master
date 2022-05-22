package digital.jayamakmur.model

import org.bson.codecs.pojo.annotations.BsonId

data class StockSoldReceipt(
    @BsonId override val id: String,
    override val product: String,
    override val stock: Long,
    override val value: Long,
    val invoice: String,
    override val metadata: Metadata,
) : StockReceipt {
    override val flow: StockReceipt.StockFlow = StockReceipt.StockFlow.SOLD
}
