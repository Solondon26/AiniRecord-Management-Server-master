package digital.jayamakmur.service.repository_service

import digital.jayamakmur.graph.AccountingAccountUpdateInput
import digital.jayamakmur.graph.Shipping
import digital.jayamakmur.model.*
import digital.jayamakmur.repository.RepositoryUnit
import digital.jayamakmur.service.EnumCode
import digital.jayamakmur.service.EnumService
import digital.jayamakmur.service.work_service.WorkService
import digital.jayamakmur.utils.MyException
import digital.jayamakmur.utils.copyFrom
import org.koin.java.KoinJavaComponent.inject

class OrderService(repository: RepositoryUnit) : RepositoryService<Order>(repository) {
    private val enum = EnumService(EnumCode.Order)
    private val product = ProductService(repository)

    override suspend fun getOrder(id: String) = repository.order.get(id) ?: throw  MyException.OrderNotFound()

    override suspend fun list() = repository.order.list()

    suspend fun create(customer: Person, shipping: Shipping, items: List<Order.OrderItem>): Order {
        items.forEach { if (product.getOrder(it.product).stock - it.qty < 0) throw MyException.ProductStockNotSufficient() }
        val document = Order(enum.getId(), customer, shipping, items, Order.Status.ACTIVE, Metadata.create())
        return repository.order.create(document)
    }

    suspend fun process(id: String) = changeStatus(id, Order.Status.PROCESSED)

    suspend fun cancel(id: String) = changeStatus(id, Order.Status.CANCELED)

    private suspend fun changeStatus(id: String, status: Order.Status): Order {
        val document = getOrder(id)
        document.status = status
        repository.order.update(id, document)
        return document
    }
}


class AccountingService(private val accountService: AccountingAccountService) : WorkService {

    private val ledgerService by inject<AccountLedgerRepository>(AccountLedgerRepository::class.java)

    suspend fun accounts() = accountService.list()

    suspend fun account(id: String) = accountService.getOrder(id)

    suspend fun createAccount(header: String, name: String) = accountService.create(header, name)

    suspend fun updateAccount(id: String, form: AccountingAccountUpdateInput) = accountService.update(id, form)

    suspend fun insertLedgerLog(account: String, ref: AccountingAccountRef, position: AccountingAccountPosition, value: Long) =
        ledgerService.create(account, ref, position, value)

    suspend fun logs() = ledgerService.list()

}

class AccountingAccountService(repository: RepositoryUnit) : RepositoryService<AccountingAccount>(repository) {
    override suspend fun getOrder(id: String) = repository.accountingAccount.get(id) ?: throw TODO("Should Throw")

    override suspend fun list() = repository.accountingAccount.list()

    suspend fun get(id: String) = repository.accountingAccount.get(id)

    suspend fun create(header: String, name: String): AccountingAccount {
        val head = header.subSequence(0, header.indexOf('0'))
        var id = ""
        for (i in 1..11) {
            if (i == 11) throw Exception("Header is Full Bro")
            id = "$head$i".padEnd(5, '0')
            get(id) ?: break
        }

        val document = AccountingAccount(id, name, Metadata.create())
        repository.accountingAccount.create(document)
        return document
    }

    suspend fun update(id: String, form: AccountingAccountUpdateInput): AccountingAccount {
        val document = getOrder(id).copyFrom(form)
        repository.accountingAccount.update(id, document)
        return document
    }
}
