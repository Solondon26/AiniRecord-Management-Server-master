val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String = "3.1.2"
var kmongo_version: String = "4.3.0"
val kgraphql_version:String = "version"
plugins {
    application
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"

}

group = "digital.jayamakmur"
version = "0.0.1"
application {
    mainClass.set("digital.jayamakmur.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")

    implementation("io.insert-koin:koin-ktor:$koin_version")

    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-id:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-id-serialization:$kmongo_version")

    implementation("io.ktor:ktor-serialization:$ktor_version")

    implementation("com.apurebase:kgraphql:$kgraphql_version")      // <-- Add these two lines
    implementation("com.apurebase:kgraphql-ktor:$kgraphql_version")

}