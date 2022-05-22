package digital.jayamakmur.graph.input

import digital.jayamakmur.model.Person

data class PersonInput(val name: String, val phone: String, val address: String) {
    fun create() = Person(name, phone, address)
}
