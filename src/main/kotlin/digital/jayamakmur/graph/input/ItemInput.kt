package digital.jayamakmur.graph.input

import digital.jayamakmur.model.Order

data class ItemInput(val product: String, val qty: Int) {
        fun create() = Order.OrderItem(product, qty)
    }
