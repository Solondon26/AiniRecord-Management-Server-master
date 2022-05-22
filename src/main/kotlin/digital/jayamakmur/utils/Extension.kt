package digital.jayamakmur.utils

import org.bson.types.ObjectId
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties

//fun getId() = ObjectId().toString()

fun <K : Any, V : Any> K.copyFrom(input: V) = copyFrom(input, this::class.declaredMemberProperties.filterIsInstance<KMutableProperty1<*, *>>())

fun <K : Any, V : Any> K.copyFrom(input: V, properties: List<KMutableProperty1<*, *>>): K {
    val b = input::class.declaredMemberProperties
    val fields = properties.filter { b.map { it.name }.contains(it.name) }
    fields.forEach { field ->
        val ref = input::class.declaredMemberProperties.find { it.name == field.name } ?: TODO()
        val value = ref.getter.call(input) ?: return@forEach
        field.setter.call(this, value)
    }
    return this
}
