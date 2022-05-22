package digital.jayamakmur.service.repository_service

import digital.jayamakmur.model.*
import digital.jayamakmur.repository.RepositoryUnit
import digital.jayamakmur.service.EnumCode
import digital.jayamakmur.service.EnumService
import digital.jayamakmur.service.work_service.InventoryService
import digital.jayamakmur.utils.MyException
import org.koin.java.KoinJavaComponent.inject

class StockReceiptService(repository: RepositoryUnit) : RepositoryService<StockReceipt>(repository) {
    private val enum = EnumService(EnumCode.StockReceipt)

    override suspend fun getOrder(id: String) = repository.stockReceipt.get(id) ?: throw  MyException.StockReceiptNotFound()

    override suspend fun list() = repository.stockReceipt.list()

    private val accountingService = Accounting2Service()
    private val inventory by inject<InventoryService>(InventoryService::class.java)

    suspend fun addStock(product: String, stock: Long, value: Long): StockInReceipt {
        val document = StockInReceipt(enum.getId(), product, stock, value, Metadata.create())
        accountingService.createProductAddStockLog(document, value)
        repository.stockReceipt.create(document)
        return document
    }

    suspend fun removeStock(product: String, stock: Long, desc: String): StockOutReceipt {
        val refProduct = inventory.getProduct(product)
        val document = StockOutReceipt(enum.getId(), product, stock, 0, desc, Metadata.create())
        accountingService.createProductRemoveStockLog(document, stock * refProduct.cost)
        repository.stockReceipt.create(document)
        return document
    }

    suspend fun sellStock(product: String, stock: Long, invoice: String): StockSoldReceipt {
        val document = StockSoldReceipt(enum.getId(), product, stock, 0, invoice, Metadata.create())
        repository.stockReceipt.create(document)
        return document
    }
}
