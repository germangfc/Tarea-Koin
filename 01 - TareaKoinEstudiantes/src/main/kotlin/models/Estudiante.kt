package org.example.models

import java.time.LocalDateTime

data class Estudiante(
    val id: Long = -1,
    val nombre: String,
    val calificacion: Double,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false
)