package org.example.repositories.estudiantes

import org.example.models.Estudiante

interface EstudiantesRepository {

    fun findAll(): List<Estudiante>
    fun findById(id: Long): Estudiante?
    fun findByCalificacion(calificacion: Double): List<Estudiante>
    fun save(producto: Estudiante): Estudiante
    fun update(id: Long, producto: Estudiante): Estudiante?
    fun delete(id: Long): Estudiante?

}