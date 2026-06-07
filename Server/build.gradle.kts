plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.kalashnikovprojects.ufmserver"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation("io.ktor:ktor-server-call-logging:3.4.0")
    implementation("io.ktor:ktor-server-rate-limit:3.4.0")
    implementation("io.ktor:ktor-server-status-pages:3.4.0")
    implementation("io.ktor:ktor-server-websockets:3.4.0")
    implementation("io.insert-koin:koin-ktor:3.4.0")
    implementation("io.insert-koin:koin-core:3.4.0")

    implementation("ch.qos.logback:logback-classic:1.4.14")

    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.postgresql:r2dbc-postgresql:1.1.1.RELEASE")

    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(libs.exposed.core)
    implementation(libs.exposed.r2dbc)
    implementation(libs.logback.classic)
    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
