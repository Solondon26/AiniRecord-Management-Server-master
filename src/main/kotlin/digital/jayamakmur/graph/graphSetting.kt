package digital.jayamakmur.graph

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import org.bson.codecs.pojo.annotations.BsonId
import org.koin.java.KoinJavaComponent.inject
import org.litote.kmongo.coroutine.CoroutineCollection

fun SchemaBuilder.graphSetting() {

    val setting = SettingService()

    mutation("setAutomationProductSale") {
        resolver { form: AutomationProductSaleInput ->
            setting.updateAutomationProductSale(
                AutomationData(form.on_stock_added.debit, form.on_stock_added.credit),
                AutomationData(form.on_stock_removed.debit, form.on_stock_removed.credit)
            )
        }
    }

    mutation("updateSetting") { resolver { id: String, value: String -> setting.updateSetting(id, value) } }

    query("getAutomationProductSale") {
        resolver { -> setting.getAutomationProductSale() }
    }

    query("getAutomationSetting") {
        resolver { -> setting.getSettings() }
    }

}

class SettingRepository(private val collection: CoroutineCollection<SettingData>) {

    suspend fun getSetting(id: String) = collection.findOneById(id) ?: throw TODO()

    suspend fun getAutomationSetting(id: String) = AutomationData(getSetting("automation.${id}.debit").value, getSetting("automation.${id}.credit").value)

    suspend fun updateAutomationData(id: String, setting: AutomationData) {
        updateSetting("automation.${id}.debit", setting.debit)
        updateSetting("automation.${id}.credit", setting.credit)
    }

    suspend fun updateSetting(id: String, value: String): SettingData {
        if (collection.findOneById(id) != null) collection.updateOneById(id, SettingData(id, value))
        else collection.insertOne(SettingData(id, value))
        return getSetting(id)
    }

    suspend fun getAll() = collection.find().toList()
}

class SettingService() {

    private val repository by inject<SettingRepository>(SettingRepository::class.java)

    suspend fun getAutomationProductSale(): AutomationProductSale =
        AutomationProductSale(repository.getAutomationSetting("on_stock_added"), repository.getAutomationSetting("on_stock_removed"))

    suspend fun updateAutomationProductSale(on_stock_added: AutomationData, on_stock_removed: AutomationData): AutomationProductSale {
        repository.updateAutomationData("on_stock_added", on_stock_added)
        repository.updateAutomationData("on_stock_removed", on_stock_removed)
        return getAutomationProductSale()
    }

    suspend fun getSettings() = repository.getAll()
    suspend fun updateSetting(id: String, value: String) = repository.updateSetting(id, value)


}

data class SettingData(@BsonId val id: String, val value: String)

data class AutomationProductSaleInput(val on_stock_added: AutomationDataInput, val on_stock_removed: AutomationDataInput)
data class AutomationProductSale(val on_stock_added: AutomationData, val on_stock_removed: AutomationData)

data class AutomationDataInput(val debit: String, val credit: String)
data class AutomationData(val debit: String, val credit: String)

const val SETTING_AUTOMATION_ON_STOCK_ADDED = "on_stock_added"
const val SETTING_AUTOMATION_ON_STOCK_REMOVED = "on_stock_removed"
const val SETTING_AUTOMATION_ON_PRODUCT_SALE = "on_sale"
const val SETTING_AUTOMATION_ON_PRODUCT_SALE_TAX = "on_sale_tax"
const val SETTING_AUTOMATION_ON_PRODUCT_SALE_DISCOUNT = "on_sale_discount"
const val SETTING_AUTOMATION_ON_PRODUCT_SALE_CREDIT = "on_sale_credit"
