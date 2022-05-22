package digital.jayamakmur.model

import org.bson.codecs.pojo.annotations.BsonId

data class AccountingAccount(
    @BsonId override var id: String,
    var name: String,
    override val metadata: Metadata
) : Document

enum class AccountingAccountPosition {
    DEBIT, CREDIT
}
