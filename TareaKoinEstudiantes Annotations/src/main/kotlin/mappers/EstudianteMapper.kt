package org.example.mappers

import database.Estudiantes
import org.example.models.Estudiante
import java.time.LocalDateTime

fun Estudiantes.toEstudiante(): Estudiante {
    return Estudiante(
        id = this.id,
        nombre = this.nombre,
        calificacion = this.calificacion,
        createdAt = LocalDateTime.parse(this.created_at),
        updatedAt = LocalDateTime.parse(this.updated_at),
        isDeleted = this.is_deleted.toInt() == 1
    )
}
