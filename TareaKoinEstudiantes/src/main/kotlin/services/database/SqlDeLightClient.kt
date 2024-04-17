package org.example.services.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.DatabaseQueries
import org.example.database.AppDatabase
import org.example.database.initDemoEstudiantes
import org.lighthousegames.logging.logging
import java.util.*

val properties = Properties()
private val log = logging()

object SqlDeLightClient {
    lateinit var databaseQueries: DatabaseQueries

    init {
        log.debug { "Inicializando el gestor de Bases de Datos con SQLDelight" }
        initConfig()
        properties.load(ClassLoader.getSystemResourceAsStream("config.properties"))
    }

    private fun initConfig() {
        //databaseQueries = if (Config.databaseInMemory) {
        val databaseInMemory = properties.getProperty("database.inmemory").toBoolean()
        databaseQueries = if (databaseInMemory) {
            log.debug { "SqlDeLightClient - InMemory" }
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        } else {
            log.debug { "SqlDeLightClient - File: ${properties.getProperty("databaseUrl")}" }
            JdbcSqliteDriver(properties.getProperty("databaseUrl"))
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
        if (properties.getProperty("databaseInitData").toBoolean()) {
        //if (Config.databaseInitData) {
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
        log.debug { "SqlDeLightClient.removeAllData()" }
        databaseQueries.transaction {
            databaseQueries.removeAllEstudiantes()
        }
    }
}