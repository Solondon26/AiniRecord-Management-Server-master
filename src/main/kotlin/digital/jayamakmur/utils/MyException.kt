package digital.jayamakmur.utils

class MyException {
    class ProductNotFound : Exception("Product Not Found")
    class StockReceiptNotFound : Exception("Stock Receipt Not Found")
    class ProductStockNotSufficient : Exception("Product Stock Not Enough")
    class OrderNotFound : Exception("Order Not Found")
    class InvoiceNotFound : Exception("Invoice Not Found")
    class OrderAlreadyCanceled : Exception("Order Already Canceled")
    class OrderAlreadyProcessed : Exception("Order Already Processed")
}