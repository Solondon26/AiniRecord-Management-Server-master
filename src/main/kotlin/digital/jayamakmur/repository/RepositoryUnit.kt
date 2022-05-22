package digital.jayamakmur.repository

import digital.jayamakmur.model.*


data class RepositoryUnit(
    val product: Repository<Product>,
    val stockReceipt: Repository<StockReceipt>,
    val order: Repository<Order>,
    val invoice: Repository<Invoice>,
    val accountingAccount: Repository<AccountingAccount>,
)

