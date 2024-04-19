package org.example

import org.example.di.EstudiantesModule
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.GlobalContext
import org.koin.fileProperties
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
fun main() {
    println("Tarea Koin Estudiantes")

    // Inicializamos Koin
    GlobalContext.startKoin {
        // declare used logger
        printLogger()
        // Leemos las propiedades de un fichero
        fileProperties("/config.properties")
        // declara modulos de inyección de dependencias, pero lo verificamos antes de inyectarlos
        EstudiantesModule.verify()
        modules(EstudiantesModule)
    }
}