package org.example.services.estudiantes

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.cache.EstudiantesCache
import org.example.errors.estudiantes.EstudianteError
import org.example.models.Estudiante
import org.example.repositories.estudiantes.EstudiantesRepository
import org.lighthousegames.logging.logging

private val log = logging()

class EstudiantesServiceImpl (
    private val estudiantesRepository: EstudiantesRepository,
    private val estudiantesCache: EstudiantesCache
): EstudiantesService {
    override fun getAll(): Result<List<Estudiante>, EstudianteError> {
        log.debug { "Obteniendo todos los estudiantes" }
        return Ok(estudiantesRepository.findAll())
    }

    override fun getByCalificacion(calificacion: Double): Result<List<Estudiante>, EstudianteError> {
        log.debug { "Obteniendo estudiantes por categoria: $calificacion" }
        return Ok(estudiantesRepository.findByCalificacion(calificacion))
    }

    override fun getById(id: Long): Result<Estudiante, EstudianteError> {
        log.debug { "Obteniendo estudiante por id: $id" }
        return estudiantesRepository.findById(id)
            ?.let { Ok(it) }
            ?: Err(EstudianteError.EstudianteNoEncontrado("Estudiante no encontrado con id: $id"))
    }

    override fun create(estudiante: Estudiante): Result<Estudiante, EstudianteError> {
        log.debug { "Guardando estudiante: $estudiante" }
        return Ok(estudiantesRepository.save(estudiante))
    }

    override fun update(id: Long, estudiante: Estudiante): Result<Estudiante, EstudianteError> {
        log.debug { "Actualizando estudiante por id: $id" }
        return estudiantesRepository.update(id, estudiante)
            ?.let { Ok(it) }
            ?: Err(EstudianteError.EstudianteNoActualizado("Estudiante no actualizado con id: $id"))
    }

    override fun delete(id: Long): Result<Estudiante, EstudianteError> {
        log.debug { "Borrando estudiante por id: $id" }
        return estudiantesRepository.delete(id)
            ?.let { Ok(it) }
            ?: Err(EstudianteError.EstudianteNoEliminado("Estudiante no eliminado con id: $id"))
    }

}