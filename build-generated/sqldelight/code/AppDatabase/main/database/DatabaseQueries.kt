package database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Double
import kotlin.Long
import kotlin.String

public class DatabaseQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectAllEstudiantes(mapper: (
    id: Long,
    nombre: String,
    calificacion: Double,
    created_at: String,
    updated_at: String,
    is_deleted: Long,
  ) -> T): Query<T> = Query(484_470_532, arrayOf("Estudiantes"), driver, "Database.sq",
      "selectAllEstudiantes",
      "SELECT Estudiantes.id, Estudiantes.nombre, Estudiantes.calificacion, Estudiantes.created_at, Estudiantes.updated_at, Estudiantes.is_deleted FROM Estudiantes") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getDouble(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!
    )
  }

  public fun selectAllEstudiantes(): Query<Estudiantes> = selectAllEstudiantes { id, nombre,
      calificacion, created_at, updated_at, is_deleted ->
    Estudiantes(
      id,
      nombre,
      calificacion,
      created_at,
      updated_at,
      is_deleted
    )
  }

  public fun <T : Any> selectEstudianteById(id: Long, mapper: (
    id: Long,
    nombre: String,
    calificacion: Double,
    created_at: String,
    updated_at: String,
    is_deleted: Long,
  ) -> T): Query<T> = SelectEstudianteByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getDouble(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!
    )
  }

  public fun selectEstudianteById(id: Long): Query<Estudiantes> = selectEstudianteById(id) { id_,
      nombre, calificacion, created_at, updated_at, is_deleted ->
    Estudiantes(
      id_,
      nombre,
      calificacion,
      created_at,
      updated_at,
      is_deleted
    )
  }

  public fun <T : Any> selectAllEstudiantesByIsDeleted(is_deleted: Long, mapper: (
    id: Long,
    nombre: String,
    calificacion: Double,
    created_at: String,
    updated_at: String,
    is_deleted: Long,
  ) -> T): Query<T> = SelectAllEstudiantesByIsDeletedQuery(is_deleted) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getDouble(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!
    )
  }

  public fun selectAllEstudiantesByIsDeleted(is_deleted: Long): Query<Estudiantes> =
      selectAllEstudiantesByIsDeleted(is_deleted) { id, nombre, calificacion, created_at,
      updated_at, is_deleted_ ->
    Estudiantes(
      id,
      nombre,
      calificacion,
      created_at,
      updated_at,
      is_deleted_
    )
  }

  public fun <T : Any> selectAllEstudiantesByCalificacion(calificacion: Double, mapper: (
    id: Long,
    nombre: String,
    calificacion: Double,
    created_at: String,
    updated_at: String,
    is_deleted: Long,
  ) -> T): Query<T> = SelectAllEstudiantesByCalificacionQuery(calificacion) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getDouble(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!
    )
  }

  public fun selectAllEstudiantesByCalificacion(calificacion: Double): Query<Estudiantes> =
      selectAllEstudiantesByCalificacion(calificacion) { id, nombre, calificacion_, created_at,
      updated_at, is_deleted ->
    Estudiantes(
      id,
      nombre,
      calificacion_,
      created_at,
      updated_at,
      is_deleted
    )
  }

  public fun <T : Any> selectEstudianteLastInserted(mapper: (
    id: Long,
    nombre: String,
    calificacion: Double,
    created_at: String,
    updated_at: String,
    is_deleted: Long,
  ) -> T): Query<T> = Query(-786_535_612, arrayOf("Estudiantes"), driver, "Database.sq",
      "selectEstudianteLastInserted",
      "SELECT Estudiantes.id, Estudiantes.nombre, Estudiantes.calificacion, Estudiantes.created_at, Estudiantes.updated_at, Estudiantes.is_deleted FROM Estudiantes WHERE id = last_insert_rowid()") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getDouble(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!
    )
  }

  public fun selectEstudianteLastInserted(): Query<Estudiantes> = selectEstudianteLastInserted { id,
      nombre, calificacion, created_at, updated_at, is_deleted ->
    Estudiantes(
      id,
      nombre,
      calificacion,
      created_at,
      updated_at,
      is_deleted
    )
  }

  public fun removeAllEstudiantes() {
    driver.execute(-1_704_237_940, """DELETE FROM Estudiantes""", 0)
    notifyQueries(-1_704_237_940) { emit ->
      emit("Estudiantes")
    }
  }

  public fun insertEstudiante(
    nombre: String,
    calificacion: Double,
    created_at: String,
    updated_at: String,
  ) {
    driver.execute(-430_954_541,
        """INSERT INTO Estudiantes (nombre, calificacion, created_at, updated_at) VALUES (?, ?, ?, ?)""",
        4) {
          bindString(0, nombre)
          bindDouble(1, calificacion)
          bindString(2, created_at)
          bindString(3, updated_at)
        }
    notifyQueries(-430_954_541) { emit ->
      emit("Estudiantes")
    }
  }

  public fun updateEstudiante(
    nombre: String,
    calificacion: Double,
    updated_at: String,
    is_deleted: Long,
    id: Long,
  ) {
    driver.execute(-1_932_572_189,
        """UPDATE Estudiantes SET nombre = ?, calificacion = ?, updated_at = ?, is_deleted = ? WHERE id = ?""",
        5) {
          bindString(0, nombre)
          bindDouble(1, calificacion)
          bindString(2, updated_at)
          bindLong(3, is_deleted)
          bindLong(4, id)
        }
    notifyQueries(-1_932_572_189) { emit ->
      emit("Estudiantes")
    }
  }

  public fun deleteEstudiante(id: Long) {
    driver.execute(2_125_227_077, """DELETE FROM Estudiantes WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(2_125_227_077) { emit ->
      emit("Estudiantes")
    }
  }

  private inner class SelectEstudianteByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Estudiantes", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Estudiantes", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_452_435_640,
        """SELECT Estudiantes.id, Estudiantes.nombre, Estudiantes.calificacion, Estudiantes.created_at, Estudiantes.updated_at, Estudiantes.is_deleted FROM Estudiantes WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Database.sq:selectEstudianteById"
  }

  private inner class SelectAllEstudiantesByIsDeletedQuery<out T : Any>(
    public val is_deleted: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Estudiantes", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Estudiantes", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-946_541_740,
        """SELECT Estudiantes.id, Estudiantes.nombre, Estudiantes.calificacion, Estudiantes.created_at, Estudiantes.updated_at, Estudiantes.is_deleted FROM Estudiantes WHERE is_deleted = ?""",
        mapper, 1) {
      bindLong(0, is_deleted)
    }

    override fun toString(): String = "Database.sq:selectAllEstudiantesByIsDeleted"
  }

  private inner class SelectAllEstudiantesByCalificacionQuery<out T : Any>(
    public val calificacion: Double,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Estudiantes", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Estudiantes", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_471_504_868,
        """SELECT Estudiantes.id, Estudiantes.nombre, Estudiantes.calificacion, Estudiantes.created_at, Estudiantes.updated_at, Estudiantes.is_deleted FROM Estudiantes WHERE calificacion = ?""",
        mapper, 1) {
      bindDouble(0, calificacion)
    }

    override fun toString(): String = "Database.sq:selectAllEstudiantesByCalificacion"
  }
}
