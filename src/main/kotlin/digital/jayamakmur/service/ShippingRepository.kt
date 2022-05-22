package digital.jayamakmur.service

import digital.jayamakmur.service.repository_service.ShippingReceipt
import org.litote.kmongo.coroutine.CoroutineCollection

class ShippingRepository(collection: CoroutineCollection<ShippingReceipt>) : MongoDBRepository<ShippingReceipt>(collection) {


}
