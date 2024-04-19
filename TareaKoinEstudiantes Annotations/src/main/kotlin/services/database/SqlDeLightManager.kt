package org.example.services.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.DatabaseQueries
import org.example.database.AppDatabase
import org.example.database.initDemoEstudiantes
import org.koin.core.annotation.Property
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging

private val log = logging()

@Singleton
class SqlDeLightManager (@Property("database.url")
                        private val _databaseUrl: String = "jdbc:sqlite:productos.db",
                         @Property("database.init.data")
                        private val _databaseInitData: String = "true",
                         @Property("database.inmemory")
                        private val _databaseInMemory: String = "true") {
    lateinit var databaseQueries: DatabaseQueries

    private val databaseUrl: String = _databaseUrl
    private val databaseInitData: Boolean = _databaseInitData.toBoolean()
    private val databaseInMemory: Boolean = _databaseInMemory.toBoolean()

    init {
        log.debug { "Inicializando el gestor de Bases de Datos con SQLDelight" }
        initConfig()
    }

    private fun initConfig() {
        databaseQueries = if (databaseInMemory) {
            log.debug { "SqlDeLightManager - InMemory" }
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        } else {
            log.debug { "SqlDeLightManager - File: ${databaseUrl}" }
            JdbcSqliteDriver(databaseUrl)
        }.let { driver ->
            // Creamos la base de datos
            log.debug { "Creando Tablas (si es necesario)" }
            AppDatabase.Schema.create(driver)
            AppDatabase(driver)
        }.databaseQueries


        // Inicializamos datos de ejemplo
        initialize()

    }

    fun initialize() {
        if (databaseInitData) {
            removeAllData()
            initDataExamples()
        }
    }

    private fun initDataExamples() {
        log.debug { "Iniciando datos de ejemplo" }
        databaseQueries.transaction {
            demoEstudiantes()
        }
    }

    private fun demoEstudiantes() {
        log.debug { "Datos de ejemplo de Productos" }
        initDemoEstudiantes().forEach {
            databaseQueries.insertEstudiante(
                nombre = it.nombre,
                calificacion = it.calificacion,
                created_at = it.createdAt.toString(),
                updated_at = it.updatedAt.toString(),
            )
        }
    }


    // limpiamos las tablas
    private fun removeAllData() {
        log.debug { "SqlDeLightManager.removeAllData()" }
        databaseQueries.transaction {
            databaseQueries.removeAllEstudiantes()
        }
    }
}