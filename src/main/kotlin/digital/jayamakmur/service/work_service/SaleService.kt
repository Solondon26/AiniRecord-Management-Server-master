package digital.jayamakmur.service.work_service

import digital.jayamakmur.graph.Shipping
import digital.jayamakmur.model.Order
import digital.jayamakmur.model.Person
import digital.jayamakmur.service.repository_service.OrderService
import digital.jayamakmur.service.repository_service.ProductService

class SaleService(private val product: ProductService, private val order: OrderService) : WorkService {

    suspend fun getProduct(id: String) = product.getOrder(id)

    suspend fun listProduct() = product.list()

    suspend fun getOrder(id: String) = order.getOrder(id)

    suspend fun listOrder() = order.list()

    suspend fun createOrder(customer: Person, shipping: Shipping, items: List<Order.OrderItem>) = order.create(customer, shipping, items)

}

