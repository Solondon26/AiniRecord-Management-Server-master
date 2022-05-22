package digital.jayamakmur.service.repository_service

import digital.jayamakmur.Config
import digital.jayamakmur.graph.PaymentInput
import digital.jayamakmur.model.*
import digital.jayamakmur.repository.RepositoryUnit
import digital.jayamakmur.service.EnumCode
import digital.jayamakmur.service.EnumService
import digital.jayamakmur.service.ShippingRepository
import digital.jayamakmur.service.work_service.InventoryService
import digital.jayamakmur.utils.MyException
import org.bson.codecs.pojo.annotations.BsonId
import org.koin.java.KoinJavaComponent.inject

class InvoiceService(repository: RepositoryUnit) : RepositoryService<Invoice>(repository) {
    private val enum = EnumService(EnumCode.Invoice)
    private val productService = ProductService(repository)
    private val orderService = OrderService(repository)
    private val shipping = ShippingService()

    override suspend fun getOrder(id: String) = repository.invoice.get(id) ?: throw  MyException.InvoiceNotFound()

    override suspend fun list() = repository.invoice.list()


    suspend fun createInvoice(order_id: String, payment: PaymentInput): Invoice {
        val order = orderService.getOrder(order_id)

        if (order.status == Order.Status.CANCELED) throw MyException.OrderAlreadyCanceled()
        if (order.status == Order.Status.PROCESSED) throw MyException.OrderAlreadyProcessed()

        val items = order.items.map { Invoice.Item(productService.getOrder(it.product), it.qty) }
        val subtotal = items.sumOf { (it.product.price) * it.qty }
        val discount = items.sumOf { (it.product.price * ((it.product.discount.toFloat()) / 100f)).toLong() * it.qty }
        val shipping_value = order.shipping.value
        val tax = (((subtotal - discount) + shipping_value) * (Config.TAX_RATE / 100f)).toLong()
        val total = ((subtotal - discount) + shipping_value) + tax

        val shipping = shipping.createShippingReceipt(order_id)

        val invoice =
            Invoice(
                enum.getId(),
                order.id,
                shipping.id,
                order.customer,
                items,
                Config.TAX_RATE,
                tax,
                subtotal,
                shipping_value,
                discount,
                listOf(Payment(payment.type, payment.value)),
                total,
                Metadata.create()
            )

        orderService.process(order_id)
        repository.invoice.create(invoice)
        return invoice
    }

    fun payment(invoice: Invoice, payment: PaymentInput) {
        repository
    }

}

data class ShippingReceipt(
    @BsonId override val id: String,
    val order_id: String,
    val destination: Person,
    val items: List<Order.OrderItem>,
    val value: Long,
    val tax: Long,
    val total: Long,
) : Document {
    @BsonId
    override val metadata = Metadata.create()
}

class ShippingService {
    private val enum = EnumService(EnumCode.ShippingReceipt)
    private val shipping by inject<ShippingRepository>(ShippingRepository::class.java)
    private val inventory by inject<InventoryService>(InventoryService::class.java)
    private val order by inject<OrderService>(OrderService::class.java)

    suspend fun getShippingReceipt(id: String) = shipping.get(id) ?: throw Exception("SHipping Receipt With ID : $id Not Found")

    suspend fun createShippingReceipt(order_id: String): ShippingReceipt {
        val order = order.getOrder(order_id)
        val tax = order.shipping.value.calcTaxShipping()
        val document = ShippingReceipt(enum.getId(),order_id, order.shipping.destination, order.items, order.shipping.value, tax, order.shipping.value + tax)
        return shipping.create(document)
    }

}


const val CONFIG_TAX_SHIPPING = .1

fun Long.calcTaxShipping() = calcTax(CONFIG_TAX_SHIPPING)

fun Long.calcTax(tax_rate: Double) = (this * tax_rate).toLong()