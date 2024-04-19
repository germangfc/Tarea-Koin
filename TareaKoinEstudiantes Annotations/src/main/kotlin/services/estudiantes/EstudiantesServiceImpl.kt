package org.example.services.estudiantes

import com.github.michaelbull.result.*
import org.example.cache.EstudiantesCache
import org.example.errors.estudiantes.EstudianteError
import org.example.models.Estudiante
import org.example.repositories.estudiantes.EstudiantesRepository
import org.koin.core.annotation.Factory
import org.lighthousegames.logging.logging

/**
 * Clase con el servicio que hará de coordinador de las peticiones y entregas de información
 * a los distintos componentes de la aplicación
 */
private val log = logging()

@Factory

class EstudiantesServiceImpl (
    private val estudiantesRepository: EstudiantesRepository,
    private val estudiantesCache: EstudiantesCache
): EstudiantesService {

    /**
     * Función que devuelve todos los estudiantes
     * @return OK => lista de estudiantes
     */
    override fun getAll(): Result<List<Estudiante>, EstudianteError> {
        log.debug { "Obteniendo todos los estudiantes" }
        return Ok(estudiantesRepository.findAll())
    }

    /**
     * Devuelve estudiantes por calificación dada
     * @param calificacion Calificación que se busca
     * @return OK => lista de estudiantes con esa calificación
     */
    override fun getByCalificacion(calificacion: Double): Result<List<Estudiante>, EstudianteError> {
        log.debug { "Obteniendo estudiantes por categoria: $calificacion" }
        return Ok(estudiantesRepository.findByCalificacion(calificacion))
    }

    override fun getById(id: Long): Result<Estudiante, EstudianteError> {

        log.debug { "Obteniendo estudiante por id: $id" }

        return estudiantesCache.get(id).mapBoth(
            success = {
                log.debug { "Estudiante encontrado en caché" }
                Ok(it)
            },
            failure = {
                log.debug { "Estudiante no encontrado en caché" }
                estudiantesRepository.findById(id)
                    ?.let { Ok(it) } // está en el repo
                    ?: Err(EstudianteError.EstudianteNoEncontrado("Estudiante no encontrado con id: $id"))
            }
        )

    }

    override fun create(estudiante: Estudiante): Result<Estudiante, EstudianteError> {
        log.debug { "Guardando estudiante: $estudiante" }

        return Ok(estudiantesRepository.save(estudiante))
            .andThen {
                log.debug { "Guardando estudiante id $it.id en caché" }
                estudiantesCache.put(it.id,it)
            }
    }

    override fun update(id: Long, estudiante: Estudiante): Result<Estudiante, EstudianteError> {
        log.debug { "Actualizando estudiante por id: $id" }

        return estudiantesRepository.update(id, estudiante)
            ?.let {
                estudiantesCache.put(id,it)
                Ok(it)
            }
            ?: Err(EstudianteError.EstudianteNoActualizado("Estudiante no actualizado con id: $id"))
    }

    override fun delete(id: Long): Result<Estudiante, EstudianteError> {
        log.debug { "Borrando estudiante por id: $id" }
        return estudiantesRepository.delete(id)
            ?.let {
                estudiantesCache.remove(id)
                Ok(it) }
            ?: Err(EstudianteError.EstudianteNoEliminado("Estudiante no eliminado con id: $id"))

    }

}