package digital.jayamakmur.service.work_service

import digital.jayamakmur.graph.PaymentInput
import digital.jayamakmur.model.Accounting2Service
import digital.jayamakmur.model.Invoice
import digital.jayamakmur.service.repository_service.InvoiceService
import digital.jayamakmur.service.repository_service.OrderService
import digital.jayamakmur.service.repository_service.ProductService

class CashierService(
    private val product: ProductService,
    private val order: OrderService,
    private val invoice: InvoiceService,
) : WorkService {

    val accounting2Service = Accounting2Service()

    suspend fun getInvoice(id: String) = invoice.getOrder(id)
    suspend fun getInvoices() = invoice.list()

    suspend fun createInvoice(orderId: String, payment: PaymentInput): Invoice {
        val document = invoice.createInvoice(orderId, payment)
        accounting2Service.createInvoicePaymentLog(document,payment)
        return document
    }

}