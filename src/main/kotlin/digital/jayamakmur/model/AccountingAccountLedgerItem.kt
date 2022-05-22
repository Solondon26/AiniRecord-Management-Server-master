package digital.jayamakmur.model

import digital.jayamakmur.graph.*
import digital.jayamakmur.service.EnumCode
import digital.jayamakmur.service.EnumService
import org.bson.codecs.pojo.annotations.BsonId
import org.koin.java.KoinJavaComponent.inject
import org.litote.kmongo.coroutine.CoroutineCollection

data class AccountingAccountLedgerItem(
    @BsonId override val id: String,
    val account: String,
    val ref: AccountingAccountRef, val position: AccountingAccountPosition?,
    val value: Long,
    override val metadata: Metadata
) : Document


class AccountLedgerRepository(private val collection: CoroutineCollection<AccountingAccountLedgerItem>) {
    private val enum = EnumService(EnumCode.LedgerLog)


    suspend fun create(account: String, ref: AccountingAccountRef, position: AccountingAccountPosition, value: Long): AccountingAccountLedgerItem {
        val document = AccountingAccountLedgerItem(enum.getId(), account, ref, position, value, Metadata.create())
        collection.insertOne(document)
        return document
    }

    suspend fun list() = collection.find().toList()

}

class AccountRepository(private val collection: CoroutineCollection<AccountingAccount>) {

    suspend fun get(id: String) = collection.findOneById(id) ?: throw TODO("Account With ID $id Was Not Found")

}

class Accounting2Service() {

    private val ledgerRepository by inject<AccountLedgerRepository>(AccountLedgerRepository::class.java)
    private val accountRepository by inject<AccountRepository>(AccountRepository::class.java)
    private val settingRepository by inject<SettingRepository>(SettingRepository::class.java)

    private suspend fun createLogPair(ref: AccountingAccountRef, debit: String, credit: String, value: Long) {
        createLog(debit, ref, AccountingAccountPosition.DEBIT, value)
        createLog(credit, ref, AccountingAccountPosition.CREDIT, value)
    }

    private suspend fun createLog(account: String, ref: AccountingAccountRef, position: AccountingAccountPosition, value: Long) =
        ledgerRepository.create(account, ref, position, value)

    suspend fun getAccount(id: String) = accountRepository.get(id)

    suspend fun createProductAddStockLog(receipt: StockReceipt, value: Long) {
        val default = settingRepository.getAutomationSetting(SETTING_AUTOMATION_ON_STOCK_ADDED)
        createLogPair(AccountingAccountRef(AccountingAccountRefType.StockReceipt, receipt.id), default.debit, default.credit, value)
    }

    suspend fun createProductRemoveStockLog(receipt: StockReceipt, value: Long) {
        val default = settingRepository.getAutomationSetting(SETTING_AUTOMATION_ON_STOCK_REMOVED)
        createLogPair(AccountingAccountRef(AccountingAccountRefType.StockReceipt, receipt.id), default.debit, default.credit, value)
    }

    suspend fun createInvoicePaymentLog(document: Invoice, payment: PaymentInput) {
        val default_sale = settingRepository.getAutomationSetting(SETTING_AUTOMATION_ON_PRODUCT_SALE)
        val default_sale_discount = settingRepository.getAutomationSetting(SETTING_AUTOMATION_ON_PRODUCT_SALE_DISCOUNT)
        val default_sale_tax = settingRepository.getAutomationSetting(SETTING_AUTOMATION_ON_PRODUCT_SALE_TAX)
        val default_sale_credit = settingRepository.getAutomationSetting(SETTING_AUTOMATION_ON_PRODUCT_SALE_CREDIT)

        val ref = AccountingAccountRef(AccountingAccountRefType.INVOICE, document.id)

        val debit = document.payment.sumOf { it.value }
        if (debit < document.total)
            createLog(default_sale_credit.debit, ref, AccountingAccountPosition.DEBIT, document.total - debit)
        createLog(default_sale.debit, ref, AccountingAccountPosition.DEBIT, payment.value - document.tax)
        createLog(default_sale.credit, ref, AccountingAccountPosition.CREDIT, document.total - document.tax)
        createLogPair(ref, default_sale_discount.debit, default_sale_discount.credit, document.discount)
        createLogPair(ref, default_sale_tax.debit, default_sale_tax.credit, document.tax)
    }
}

data class AccountingAccountRef(val type: AccountingAccountRefType, val id: String)

enum class AccountingAccountRefType {
    StockReceipt,
    INVOICE
}