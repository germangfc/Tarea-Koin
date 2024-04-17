package org.example.errors.estudiantes


sealed class EstudianteError(val message: String) {
    class EstudianteNoEncontrado(message: String) : EstudianteError(message)
    class EstudianteNoValido(message: String) : EstudianteError(message)
    class EstudianteNoActualizado(message: String) : EstudianteError(message)
    class EstudianteNoEliminado(message: String) : EstudianteError(message)
}