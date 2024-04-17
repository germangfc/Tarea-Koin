package org.example.repositories.estudiantes

import org.example.mappers.toEstudiante
import org.example.models.Estudiante
import org.example.services.database.SqlDeLightClient
import org.lighthousegames.logging.logging
import java.time.LocalDateTime

private val log = logging()

class EstudiantesRepositoryImpl:EstudiantesRepository {

    private val db = SqlDeLightClient.databaseQueries

    /**
     * Obtiene todos los estudiantes de la base de datos
     */
    override fun findAll(): List<Estudiante> {
        log.debug { "Obteniendo todos los estudiantes" }
        return db.selectAllEstudiantes()
            .executeAsList()
            .map { it.toEstudiante() }
    }

    /**
     * Obtiene un estudiante por id
     */
    override fun findById(id: Long): Estudiante? {
        // TODO buscar usando la caché
        log.debug { "Obteniendo Estudiante por id: $id" }
        return db.selectEstudianteById(id)
            .executeAsOneOrNull()
            ?.toEstudiante()
    }

    /**
     * Obtiene estudiantes por calificación
     */
    override fun findByCalificacion(calificacion: Double): List<Estudiante> {
        log.debug { "Obteniendo estudiantes por calificación: $calificacion" }
        return db.selectAllEstudiantesByCalificacion(calificacion)
            .executeAsList()
            .map { it.toEstudiante() }
    }

    /**
     * Guarda un estudiante en la base de datos
     */
    override fun save(estudiante: Estudiante): Estudiante {
        // TODO usar la caché

        log.debug { "Guardando estudiante: $estudiante" }

        val timeStamp = LocalDateTime.now().toString()

        db.transaction {
            db.insertEstudiante(
                nombre = estudiante.nombre,
                calificacion = estudiante.calificacion,
                created_at = timeStamp,
                updated_at = timeStamp
            )
        }
        return db.selectEstudianteLastInserted()
            .executeAsOne()
            .toEstudiante()
    }

    /**
     * Actualiza un estudiante en la base de datos
     */
    override fun update(id: Long, estudiante: Estudiante): Estudiante? {
        log.debug { "Actualizando estudiante por id: $id" }
        var result = this.findById(id) ?: return null
        val timeStamp = LocalDateTime.now()

        result = result.copy(
            nombre = estudiante.nombre,
            calificacion = estudiante.calificacion,
            isDeleted = estudiante.isDeleted
        )

        db.updateEstudiante(
            nombre = result.nombre,
            calificacion = estudiante.calificacion,
            is_deleted = if (result.isDeleted) 1 else 0,
            updated_at = timeStamp.toString(),
            id = estudiante.id,
        )
        return result
    }

    /**
     * Borra un estudiante de la base de datos
     */
    override fun delete(id: Long): Estudiante? {
        log.debug { "Borrando estudiante por id: $id" }
        val result = this.findById(id) ?: return null
        db.deleteEstudiante(id)
        return result
    }
}