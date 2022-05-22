package digital.jayamakmur.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "flow")
@JsonSubTypes(
    Type(value = StockInReceipt::class, name = "IN"),
    Type(value = StockOutReceipt::class, name = "OUT"),
    Type(value = StockSoldReceipt::class, name = "SOLD"),
)
interface StockReceipt : Document {
    val product: String
    val stock: Long
    val value: Long
    val flow: StockFlow

    enum class StockFlow { IN, OUT, SOLD }
}