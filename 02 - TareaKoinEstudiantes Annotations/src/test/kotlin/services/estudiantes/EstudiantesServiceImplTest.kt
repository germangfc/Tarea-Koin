package services.estudiantes

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.example.cache.EstudiantesCache
import org.example.errors.cache.CacheError
import org.example.errors.estudiantes.EstudianteError
import org.example.models.Estudiante
import org.example.repositories.estudiantes.EstudiantesRepository
import org.example.services.estudiantes.EstudiantesServiceImpl
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)

class EstudiantesServiceImplTest {


    @MockK
    private lateinit var repoMock: EstudiantesRepository

    @MockK
    private lateinit var cacheMock:EstudiantesCache

    @InjectMockKs
    private lateinit var service:EstudiantesServiceImpl


    @Test
    fun getAll() {
        val expectedEstudiantes = listOf(
            Estudiante(
                id=1,
                nombre = "Alumno 1",
                calificacion = 10.0,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                isDeleted = false
            )
        )

        every { repoMock.findAll() } returns expectedEstudiantes

        val estudiantes = service.getAll().value

        assertAll(
            { assertEquals(1, estudiantes.size) },
            { assertEquals("Alumno 1", estudiantes[0].nombre) },
            { assertEquals(10.0, estudiantes[0].calificacion) }
        )

        verify (exactly = 1) { repoMock.findAll() }
    }

    @Test
    fun getByCalificacion() {
        val calif:Double = 10.0
        val expectedEstudiantes = listOf(
            Estudiante(
                id=1,
                nombre = "Alumno 1",
                calificacion = 10.0,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                isDeleted = false
            )
        )

        every { repoMock.findByCalificacion(calif) } returns expectedEstudiantes

        val estudiantes = service.getByCalificacion(calif).value

        assertAll(
            { assertEquals(1, estudiantes.size) },
            { assertEquals("Alumno 1", estudiantes[0].nombre) },
            { assertEquals(10.0, estudiantes[0].calificacion) }
        )

        verify (exactly = 1) { repoMock.findByCalificacion(calif) }
    }

    @Test
    // Encuentra en la caché y ya no busca en el repo
    fun getByIdCacheFound(){
        val id:Long = 1
        val expectedEstud = Estudiante(
            id=id,
            nombre = "Alumno 1",
            calificacion = 10.0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            isDeleted = false
        )

        every { cacheMock.get(1) } returns Ok(expectedEstud)

        val estud = service.getById(1).value

        assertAll(
            { assertEquals(expectedEstud.id, estud.id) },
            { assertEquals(expectedEstud.nombre, estud.nombre) },
            { assertEquals(expectedEstud.calificacion, estud.calificacion ) }
        )

        verify (exactly = 1) {cacheMock.get(1)}
        verify (exactly = 0) {repoMock.findById(1 )}
    }

    @Test
    // No encuentra en caché y sí en repositorio
    fun getById() {
        val id:Long = 1
        val expectedEstud = Estudiante(
            id=1,
            nombre = "Alumno 1",
            calificacion = 10.0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            isDeleted = false
        )
        every { cacheMock.get(id) } returns Err(CacheError("Item no encontrado en cache"))
        every { repoMock.findById(id) } returns expectedEstud

        val estud = service.getById(id).value

        assertAll(
            { assertEquals(expectedEstud.id, estud.id) },
            { assertEquals(expectedEstud.nombre, estud.nombre) },
            { assertEquals(expectedEstud.calificacion, estud.calificacion ) }
        )

        verify(exactly = 1) { repoMock.findById(id) }
        verify(exactly = 1) { cacheMock.get(id) }
    }

    @Test
    // No encuentra ni en la caché ni en el repo
    fun getByIdNotFound() {
        val id = 1L

        every { cacheMock.get(id) } returns Err(CacheError("Item no encontrado en cache"))
        every { repoMock.findById(id) } returns null

        val error = service.getById(id).error

        assertAll(
            { assertTrue(error is EstudianteError.EstudianteNoEncontrado) },
            { assertEquals("Estudiante no encontrado con id: $id", error.message) }
        )

        verify(exactly = 1) { repoMock.findById(id) }
        verify(exactly = 1) { cacheMock.get(id) }
    }

    @Test
    fun create() {
        val expectedEstud = Estudiante(
            id = 1L,
            nombre = "Nuevo Alumno",
            calificacion = 10.0
        )

        every { repoMock.save(expectedEstud) } returns expectedEstud.copy(id = 1)
        every { cacheMock.put(1, expectedEstud.copy(id = 1)) } returns Ok(expectedEstud.copy(id = 1))

        val estudiante = service.create(expectedEstud).value

        assertAll(
            { assertEquals(expectedEstud.id, estudiante.id) },
            { assertEquals(expectedEstud.nombre, estudiante.nombre) },
            { assertEquals(expectedEstud.calificacion, estudiante.calificacion ) }
        )

        verify(exactly = 1) { repoMock.save(expectedEstud) }
        verify(exactly = 1) { cacheMock.put(1, expectedEstud.copy(id = 1)) }
    }


    @Test
    fun updateOk() {

        val id = 1L
        val updatedEstudiante = Estudiante(
            id = id,
            nombre = "Alumno UPDATE",
            calificacion = 5.0
        )

        every { repoMock.update(id, updatedEstudiante) } returns updatedEstudiante
        every { cacheMock.put(id, updatedEstudiante.copy(id = id)) } returns Ok(updatedEstudiante.copy(id = id))

        val estudiante = service.update(id, updatedEstudiante).value

        assertAll(
            { assertEquals(updatedEstudiante.id, estudiante.id) },
            { assertEquals(updatedEstudiante.nombre, estudiante.nombre) },
            { assertEquals(updatedEstudiante.calificacion, estudiante.calificacion ) }
        )

        verify(exactly = 1) { repoMock.update(id, updatedEstudiante) }
        verify(exactly = 1) { cacheMock.put(id, updatedEstudiante.copy(id = id)) }
    }

    @Test
    fun updateNotUpdated() {
        val id = 1L
        val updatedEstudiante = Estudiante(
            id = id,
            nombre = "Alumno UPDATE",
            calificacion = 5.0
        )

        every { repoMock.update(id, updatedEstudiante) } returns null

        val error = service.update(id, updatedEstudiante).error

        assertAll(
            { assertTrue(error is EstudianteError.EstudianteNoActualizado) },
            { assertEquals("Estudiante no actualizado con id: $id", error.message) }
        )

        verify(exactly = 1) { repoMock.update(id, updatedEstudiante) }
    }
    @Test
    fun delete() {

        val id = 1L
        val deletedEstudiante = Estudiante(
            id = id,
            nombre = "Alumno 1",
            calificacion = 10.0
        )

        every { repoMock.delete(id) } returns deletedEstudiante
        every { cacheMock.remove(id) } returns Ok(deletedEstudiante.copy(id = id))

        val estudiante = service.delete(id).value

        assertAll(
            { assertEquals(deletedEstudiante.id, estudiante.id) },
            { assertEquals(deletedEstudiante.nombre, estudiante.nombre) },
            { assertEquals(deletedEstudiante.calificacion, estudiante.calificacion ) }
        )

        verify(exactly = 1) { repoMock.delete(id) }
        verify(exactly = 1) { cacheMock.remove(id) }
    }

    @Test
    fun deleteNotFound() {
        val id = 1L

        every { repoMock.delete(id) } returns null

        val error = service.delete(id).error

        assertAll(
            { assertTrue(error is EstudianteError.EstudianteNoEliminado) },
            { assertEquals("Estudiante no eliminado con id: $id", error.message) }
        )

        verify(exactly = 1) { repoMock.delete(id) }
    }

}