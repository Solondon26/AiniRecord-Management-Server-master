package digital.jayamakmur.graph

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import digital.jayamakmur.service.work_service.CashierService

fun SchemaBuilder.graphCashier(service: CashierService) {

    query("Invoice") { resolver { id: String -> service.getInvoice(id) } }
    query("Invoices") { resolver { -> service.getInvoices() } }

    mutation("InvoiceCreate") { resolver { form: CreateInvoiceInput -> service.createInvoice(form.order,form.payment) } }
}

data class CreateInvoiceInput(val order: String, val payment: PaymentInput)
data class PaymentInput(
    val type: String,
    val value: Long
)