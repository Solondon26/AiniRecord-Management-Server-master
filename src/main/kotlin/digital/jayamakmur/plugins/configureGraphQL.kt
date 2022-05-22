package digital.jayamakmur.plugins

import com.apurebase.kgraphql.GraphQL
import digital.jayamakmur.graph.*
import digital.jayamakmur.model.StockInReceipt
import digital.jayamakmur.model.StockOutReceipt
import digital.jayamakmur.model.StockReceipt
import io.ktor.application.*
import org.koin.ktor.ext.get


fun Application.configureGraphQL() {
    install(GraphQL) {
        playground = true
        schema {
            configure {
                useDefaultPrettyPrinter = true
            }

            graphShipping()
            graphSetting()
            graphInventory(get())
            graphSale(get())
            graphCashier(get())
            graphAccounting(get())

            type<StockInReceipt>()
            type<StockOutReceipt>()
            enum<StockReceipt.StockFlow>()
        }
    }
}

