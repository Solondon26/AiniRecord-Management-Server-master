package digital.jayamakmur.service.work_service

import digital.jayamakmur.graph.ProductCreateInput
import digital.jayamakmur.graph.ProductUpdateInput
import digital.jayamakmur.service.repository_service.ProductService
import digital.jayamakmur.service.repository_service.StockReceiptService

class InventoryService(private val product: ProductService, private val stockReceipt: StockReceiptService) : WorkService {

    suspend fun getProduct(id: String) = product.getOrder(id)

    suspend fun listProduct() = product.list().sortedBy { it.name }

    suspend fun listStockReceipt() = stockReceipt.list()

    suspend fun getProductReceipt(productID: String) = listStockReceipt().filter { it.product == productID }

    suspend fun getStockReceipt(id: String) = stockReceipt.getOrder(id)

    suspend fun createProduct(form: ProductCreateInput) = product.create(form)

    suspend fun updateProduct(id: String, form: ProductUpdateInput) = product.update(id, form)


    suspend fun deleteProduct(id: String) = product.delete(id)

    suspend fun addStock(id: String, stock: Long, value: Long) = product.addStock(id, stock, value)

    suspend fun removeStock(id: String, stock: Long, desc: String) = product.removeStock(id, stock, desc)
    suspend fun sellStock(id: String, invoice: String, qty: Long) = product.sellStock(id, invoice, qty)
    suspend fun createProduct(name: String, category: String, sku: String) = product.create(name, category, sku)

}


