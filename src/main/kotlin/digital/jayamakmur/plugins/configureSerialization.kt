package digital.jayamakmur.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
            serializersModule = IdKotlinXSerializationModule
        })
    }
}