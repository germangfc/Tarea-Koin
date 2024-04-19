package repositories.estudiantes

import org.example.models.Estudiante
import org.example.repositories.estudiantes.EstudiantesRepository
import org.example.services.database.SqlDeLightManager
import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.startKoin
import org.koin.fileProperties
import org.koin.ksp.generated.defaultModule
import org.koin.test.inject
import org.koin.test.junit5.AutoCloseKoinTest
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EstudiantesRepositoryImplTest:AutoCloseKoinTest() {

    private val dbManager:SqlDeLightManager by inject()
    private val estudRepository:EstudiantesRepository by inject()

    @BeforeAll
    fun setUpAll(){
        // Inicializamos los modulos que necesitamos
        println("Inicializando todos los tests...")
        startKoin {
            fileProperties("/config.properties")
            modules(defaultModule)
        }
    }

    @BeforeEach
    fun setUp() {
        //Inicializamos la BD
        dbManager.initialize()
    }

    @Test
    fun findAll() {
        val estudiantes = estudRepository.findAll()
        assertEquals(10, estudiantes.size)
    }

    @Test
    fun findById() {
        val estud = estudRepository.findById(1)
        assertAll(
            { assertEquals(1, estud?.id) },
            { assertEquals("Alumno 1", estud?.nombre) },
            { assertEquals(10.0, estud?.calificacion) }
        )
    }

    @Test
    fun findByIdNotFound() {
        val estud = estudRepository.findById(999)
        assertEquals(null, estud)
    }

    @Test
    fun findByCalificacion() {
        val estudiantes = estudRepository.findByCalificacion(10.0)

        assertEquals(3, estudiantes.size)

    }

    @Test
    fun findByCalificacionNotFo () {
        val estudiantes = estudRepository.findByCalificacion(0.0)

        assertEquals(0, estudiantes.size)

    }

    @Test
    fun save() {
        val estud = estudRepository.save(
            Estudiante(
                nombre = "Alumno Test",
                calificacion = 10.0
            )
        )
        assertAll (
            { assertEquals(11, estud.id) },
            { assertEquals("Alumno Test", estud.nombre) },
            { assertEquals(10.0, estud.calificacion) }
        )
    }

    @Test
    fun update() {
        val estud = estudRepository.update(
            1, Estudiante(
                id = 1,
                nombre = "UPDATED STUDENT",
                calificacion = 4.0
            )
        )

        assertAll(
            { assertEquals(1, estud?.id) },
            { assertEquals("UPDATED STUDENT", estud?.nombre) },
            { assertEquals(4.0, estud?.calificacion) }
        )
    }

    @Test
    fun updateNotFound() {
        val estud = estudRepository.update(
            999,
            Estudiante(
                id = 999,
                nombre = "UPDATED STUDENT",
                calificacion = 4.0
            )
        )
        assertEquals(null, estud)
    }

    @Test
    fun delete() {
        val estud = estudRepository.delete(1)
        assertAll(
            { assertEquals(1, estud?.id) },
            { assertEquals("Alumno 1", estud?.nombre) },
            { assertEquals(10.0, estud?.calificacion) },
        )
    }

    @Test
    fun deleteNotFound() {
        val estud = estudRepository.delete(999)
        assertEquals(null, estud)
    }
}