package digital.jayamakmur.plugins

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class MongoDatabase(host: String, port: String, username: String, password: String) {
    private val client = KMongo.createClient("mongodb://$username:$password@$host:$port").coroutine //use coroutine extension
    val database = client.getDatabase("AiniRecord-Management")

    suspend fun reset(){
        database.drop()
    }

    constructor() : this("24.24.24.24", "27017", "Wiratama", "751862943")
}
