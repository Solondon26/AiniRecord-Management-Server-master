package digital.jayamakmur.graph

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import digital.jayamakmur.graph.input.ItemInput
import digital.jayamakmur.graph.input.PersonInput
import digital.jayamakmur.model.Order
import digital.jayamakmur.model.Person
import digital.jayamakmur.service.work_service.SaleService

fun SchemaBuilder.graphSale(service: SaleService) {


    fun List<ItemInput>.create() = map { it.create() }

    query("Order") { resolver { id: String -> service.getOrder(id) } }
    query("Orders") { resolver { -> service.listOrder() } }

    mutation("OrderCreate") {
        resolver { form: OrderInput ->
            service.createOrder(form.customer.create(), form.shipping.create(), form.items.create())
        }
    }

    enum<Order.Status>()
}

data class OrderInput(
    val customer: PersonInput,
    val shipping: ShippingInput,
    val items: List<ItemInput>
) {
    data class ItemInput(val product: String, val qty: Long)

}
fun List<OrderInput.ItemInput>.create() = map { Order.OrderItem(it.product, it.qty.toInt()) }

data class Shipping(val destination: Person, val value: Long)

data class ShippingInput(val destination: PersonInput, val value: Long) : GraphInput<Shipping> {
    override fun create() = Shipping(destination.create(), value)

}

interface GraphInput<T> {
    fun create(): T
}