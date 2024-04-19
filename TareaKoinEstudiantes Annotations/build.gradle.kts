plugins {
    kotlin("jvm") version "1.9.21"
    id("org.jetbrains.dokka") version "1.9.10"
    id("app.cash.sqldelight") version "2.0.2"
    // Plugin de Koin KSP
    id("com.google.devtools.ksp") version "1.9.21-1.0.16"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Mock
    testImplementation("io.mockk:mockk:1.13.10")

    // Log
    implementation("org.lighthousegames:logging:1.3.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Koin, con BOM ya se instalan todas las dependencias necesarias con la versión correcta
    implementation(platform("io.insert-koin:koin-bom:3.5.6"))
    implementation("io.insert-koin:koin-core") // Core
    implementation("io.insert-koin:koin-test") // Para test y usar checkModules
    // Para las anotaciones
    implementation(platform("io.insert-koin:koin-annotations-bom:1.3.1")) // BOM
    implementation("io.insert-koin:koin-annotations") // Annotations
    ksp("io.insert-koin:koin-ksp-compiler:1.3.1") // KSP Compiler, el mismo que el de las anotaciones
    // Koin
    testImplementation("io.insert-koin:koin-test-junit5") // Para test con JUnit5

    // SQLDelight para SQLite
    implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
    // Result ROP
    implementation("com.michael-bull.kotlin-result:kotlin-result:2.0.0")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

// Configuración del plugin de SqlDeLight
sqldelight {
    databases {
        // Nombre de la base de datos
        create("AppDatabase") {
            // Paquete donde se generan las clases
            packageName.set("org.example.database")
        }
    }
}