package org.example

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import org.example.models.Estudiante
import org.example.services.estudiantes.EstudiantesService
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.fileProperties
import org.koin.ksp.generated.defaultModule
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
fun main() {
    println("Tarea Koin Estudiantes con Annotations")

    // Inicializamos Koin
    startKoin {
        // declare used logger
        printLogger()
        // Leemos las propiedades de un fichero
        fileProperties("/config.properties")
        // declara modulos de inyección de dependencias, pero lo verificamos antes de inyectarlos
        defaultModule.verify(
            extraTypes = listOf(
                Boolean::class,
                Int::class
            )
        ) // Verificamos que los módulos están bien configurados antes de inyectarlos (ver test!)
        modules(defaultModule)
    }

    val app = EstudiantesApp()
    app.run()

}

class EstudiantesApp : KoinComponent {
    val estudiantesService: EstudiantesService by inject()

    fun run() {
        println("Buscando estudiantes")
        estudiantesService.getAll()
            .onSuccess { est-> est.forEach { println(it) } }

        println()
        println("Buscando estudiante por id...")
        estudiantesService.getById(1)
            .onSuccess { println("Encontrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }


        println("Buscando estudiante por id...")
        estudiantesService.getById(99)
            .onSuccess { println("Encontrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }

        println()
        println("Creando estudiante...")
        val estudiante = Estudiante(
            nombre = "Alumno 11",
            calificacion = 10.0
        )
        estudiantesService.create(estudiante)
            .onSuccess { println("Creado: $it") }
            .onFailure { println("ERROR: ${it.message}") }

        println()
        println("Actualizando estudiantes...")
        estudiantesService.update(1, estudiante.copy(nombre = "Alumno 1 Modificado"))
            .onSuccess { println("Actualizado: $it") }
            .onFailure { println("ERROR: ${it.message}") }


        println()
        println("Buscando estudiante...")
        estudiantesService.getById(1)
            .onSuccess { println("Encontrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }

        println()
        println("Buscando estudiantes...")
        estudiantesService.getAll()
            .onSuccess { estudiantes ->
                estudiantes.forEach { println(it) }
            }

        println()
        println("Borrando estudiante por id...")
        estudiantesService.delete(1)
            .onSuccess { println("Borrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }

        println()
        println()
        println("Borrando estudiante por id...")
        estudiantesService.delete(99)
            .onSuccess { println("Borrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }


        println()
        println("Buscando estudiante...")
        estudiantesService.getById(1)
            .onSuccess { println("Encontrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }

        println()
        println("Buscando estudiantes...")
        println(estudiantesService.getAll())

        println()
        println("Buscando estudiantes por calificación...")
        println(estudiantesService.getByCalificacion(10.0))
        
    }
}
