package database

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Estudiantes(
  public val id: Long,
  public val nombre: String,
  public val calificacion: Double,
  public val created_at: String,
  public val updated_at: String,
  public val is_deleted: Long,
)
