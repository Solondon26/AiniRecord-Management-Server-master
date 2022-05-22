package digital.jayamakmur.service.repository_service

import digital.jayamakmur.graph.ProductCreateInput
import digital.jayamakmur.graph.ProductUpdateInput
import digital.jayamakmur.model.Accounting2Service
import digital.jayamakmur.model.Metadata
import digital.jayamakmur.model.Product
import digital.jayamakmur.repository.RepositoryUnit
import digital.jayamakmur.service.EnumCode
import digital.jayamakmur.service.EnumService
import digital.jayamakmur.utils.MyException
import digital.jayamakmur.utils.copyFrom


class ProductService(repository: RepositoryUnit) : RepositoryService<Product>(repository) {
    private val enum = EnumService(EnumCode.Product)
    private val receiptService = StockReceiptService(repository)
    private val accountingService = Accounting2Service()

    override suspend fun getOrder(id: String) = repository.product.get(id) ?: throw  MyException.ProductNotFound()

    override suspend fun list() = repository.product.list()

    suspend fun create(form: ProductCreateInput): Product {
        val product = Product(enum.getId(), form.name, form.category, form.price, sku = form.sku, metadata = Metadata.create())
        repository.product.create(product)
        return product
    }

    suspend fun update(id: String, form: ProductUpdateInput): Product {
        val product = getOrder(id).copyFrom(form)
        repository.product.update(id, product)
        return product
    }


    suspend fun delete(id: String) = repository.product.delete(id)

    suspend fun addStock(product: String, stock: Long, value: Long): Product {
        val document = getOrder(product)
        document.cost = ((document.cost * document.stock) + value) / (document.stock + stock)
        document.stock += stock
        receiptService.addStock(product, stock, value)
        repository.product.update(product, document)
        return document
    }

    suspend fun removeStock(product: String, stock: Long, desc: String): Product {
        val document = getOrder(product)
        document.stock -= stock
        if (document.stock < 0) MyException.ProductStockNotSufficient()
        receiptService.removeStock(product, stock, desc)
        repository.product.update(product, document)
        return document
    }

    suspend fun sellStock(product: String, invoice: String, qty: Long): Product {
        val document = getOrder(product)
        document.stock -= qty
        if (document.stock < 0) MyException.ProductStockNotSufficient()
        receiptService.sellStock(product, qty, invoice)
        repository.product.update(product, document)
        return document
    }

    suspend fun create(name: String, category: String, sku: String): Product {
        val product = Product(enum.getId(), name, category, 0, sku = sku, metadata = Metadata.create())
        repository.product.create(product)
        return product
    }
}

