package digital.jayamakmur.graph

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import digital.jayamakmur.model.Metadata
import digital.jayamakmur.model.Order
import digital.jayamakmur.model.Product
import digital.jayamakmur.model.StockReceipt
import digital.jayamakmur.service.work_service.InventoryService

fun SchemaBuilder.graphInventory(service: InventoryService) {

    query("Product") { resolver { id: String -> service.getProduct(id) } }
    query("Products") { resolver { -> service.listProduct() } }

    mutation("ProductCreate") { resolver { name: String, category: String, sku: String -> service.createProduct(name,category,sku) } }

    mutation("ProductUpdate") {
        resolver { id: String, form: ProductUpdateInput ->
            service.updateProduct(id, form)
        }
    }

    mutation("ProductAddStock") { resolver { id: String, stock: Long, value: Long -> service.addStock(id, stock, value) } }
    mutation("ProductRemoveStock") { resolver { id: String, stock: Long, desc: String -> service.removeStock(id, stock, desc) } }
    mutation("ProductDelete") { resolver { id: String -> service.deleteProduct(id) } }

    query("StockReceipt") { resolver { id: String -> service.getStockReceipt(id) } }
    query("ProductStockReceipts") { resolver { id: String -> service.listStockReceipt().filter { it.product == id } } }
    query("StockReceipts") { resolver { -> service.listStockReceipt() } }

    type<StockReceipt> {
        StockReceipt::product.ignore()
        property<Product>("product") { resolver { ref: StockReceipt -> service.getProduct(ref.product) } }
    }
    type<Order.OrderItem> {
        Order.OrderItem::product.ignore()
        property<Product>("product") { resolver { ref: Order.OrderItem -> service.getProduct(ref.product) } }
    }

    enum<Metadata.MetadataStatus>()
}

data class ProductUpdateInput(
    val name: String?,
    val category: String?,
    val price: Long?,
    val discount: Int?,
    val cost: Long?,
    val rop: Long?,
    val sku: String?,
)

