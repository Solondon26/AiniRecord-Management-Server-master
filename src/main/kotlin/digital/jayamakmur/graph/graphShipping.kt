package digital.jayamakmur.graph

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import digital.jayamakmur.service.repository_service.ShippingService

fun SchemaBuilder.graphShipping() {
    val shipping = ShippingService()

    query("ShippingReceipt") {
        resolver { id: String -> shipping.getShippingReceipt(id) }
    }
}
