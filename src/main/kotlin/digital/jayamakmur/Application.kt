package digital.jayamakmur

import digital.jayamakmur.graph.SettingRepository
import digital.jayamakmur.model.AccountLedgerRepository
import digital.jayamakmur.model.AccountRepository
import digital.jayamakmur.plugins.MongoDatabase
import digital.jayamakmur.plugins.configureCORS
import digital.jayamakmur.plugins.configureGraphQL
import digital.jayamakmur.plugins.configureSerialization
import digital.jayamakmur.repository.RemoteRepository
import digital.jayamakmur.repository.RepositoryUnit
import digital.jayamakmur.service.EnumRepository
import digital.jayamakmur.service.ShippingRepository
import digital.jayamakmur.service.repository_service.*
import digital.jayamakmur.service.work_service.CashierService
import digital.jayamakmur.service.work_service.InventoryService
import digital.jayamakmur.service.work_service.SaleService
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.koin.core.context.startKoin
import org.koin.dsl.module


fun main() {
    embeddedServer(CIO, port = 8080, host = "24.24.24.24") {
        startKoin {
            modules(PRODUCTION_MODULE)
        }
        configureCORS()
        configureSerialization()
        configureGraphQL()
    }.start(wait = true)
}


val PRODUCTION_MODULE = module {
    val database = MongoDatabase().database
    val repository = RepositoryUnit(
        RemoteRepository(database.getCollection("Product")),
        RemoteRepository(database.getCollection("StockReceipt")),
        RemoteRepository(database.getCollection("Order")),
        RemoteRepository(database.getCollection("Invoice")),
        RemoteRepository(database.getCollection("AccountingAccount")),
    )

    single { AccountLedgerRepository(database.getCollection("AccountingAccountLedger")) }
    single { AccountRepository(database.getCollection("AccountingAccount")) }
    single { SettingRepository(database.getCollection("Setting")) }

    val product = ProductService(repository)
    val stockReceipt = StockReceiptService(repository)
    val order = OrderService(repository)
    val invoice = InvoiceService(repository)
    val accountingAccount = AccountingAccountService(repository)

    single { OrderService(repository) }
    single { ShippingRepository(database.getCollection("ShippingReceipt")) }
    single { EnumRepository(database.getCollection("Enum")) }

    single { InventoryService(product, stockReceipt) }
    single { SaleService(product, order) }
    single { CashierService(product, order, invoice) }
    single { AccountingService(accountingAccount) }

}
