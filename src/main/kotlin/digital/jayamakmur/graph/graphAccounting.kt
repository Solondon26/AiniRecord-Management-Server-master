package digital.jayamakmur.graph

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import digital.jayamakmur.model.AccountingAccountPosition
import digital.jayamakmur.model.AccountingAccountRef
import digital.jayamakmur.model.AccountingAccountRefType
import digital.jayamakmur.service.repository_service.AccountingService

fun SchemaBuilder.graphAccounting(service: AccountingService) {

    query("AccountingAccount") {
        resolver { id: String -> service.account(id) }
    }

    query("AccountingAccounts") {
        resolver { -> service.accounts() }
    }

    mutation("AccountingAccountCreate") {
        resolver { header: String, name: String -> service.createAccount(header, name) }
    }

    mutation("AccountingAccountUpdate") {
        resolver { id: String, form: AccountingAccountUpdateInput ->
            service.updateAccount(id, form)
        }
    }

    mutation("insertLedgerLog") {
        resolver { account: String, refType: AccountingAccountRefType, refId: String, position: AccountingAccountPosition, value: Long ->
            service.insertLedgerLog(account, AccountingAccountRef(refType, refId), position, value)
        }
    }

    query("LedgerLogs") {
        resolver { -> service.logs() }
    }

    enum<AccountingAccountPosition>()
    enum<AccountingAccountRefType>()
}

data class AccountingAccountUpdateInput(val name: String?, val normal: AccountingAccountPosition?)

data class ProductCreateInput(
    val name: String,
    val category: String,
    val price: Long,
    val sku: String
)

