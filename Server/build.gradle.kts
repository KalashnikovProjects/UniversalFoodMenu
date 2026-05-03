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
    implementation("io.insert-koin:koin-ktor:3.5.3")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.postgresql:r2dbc-postgresql:1.1.1.RELEASE")
    implementation("io.ktor:ktor-server-websockets:2.3.5")
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
