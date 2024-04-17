package org.example.services.estudiantes

import com.github.michaelbull.result.Result
import org.example.errors.estudiantes.EstudianteError
import org.example.models.Estudiante

interface EstudiantesService {
    fun getAll(): Result<List<Estudiante>, EstudianteError>
    fun getByCalificacion(calificacion: Double): Result<List<Estudiante>, EstudianteError>
    fun getById(id: Long): Result<Estudiante, EstudianteError>
    fun create(producto: Estudiante): Result<Estudiante, EstudianteError>
    fun update(id: Long, producto: Estudiante): Result<Estudiante, EstudianteError>
    fun delete(id: Long): Result<Estudiante, EstudianteError>

}