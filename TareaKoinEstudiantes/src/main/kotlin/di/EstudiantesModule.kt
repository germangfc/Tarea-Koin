package org.example.di

import org.example.cache.CacheImpl
import org.example.models.Estudiante
import org.example.repositories.estudiantes.EstudiantesRepository
import org.example.repositories.estudiantes.EstudiantesRepositoryImpl
import org.example.services.estudiantes.EstudiantesServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val EstudiantesModule = module {

    // Creamos repositorios de estudiantes
    single<EstudiantesRepository>(named("DataBaseRepository")) {
        EstudiantesRepositoryImpl() }

    factory<CacheImpl<Long,Estudiante>>(named("EstudiantesCache")) {
        CacheImpl(getProperty("cache_size") ) }

    // PersonasController = EstudiantesServiceImpl
    // Creamos el servicio, a demas podemos inyectar cuando queramos cualquier propiedad
    // para saber con que estamos trabajando, por ejemplo, la url del servidor
    // de nuevo usamos el named para indicar cuál es el repositorio que queremos usar
    // llamaremos a PersonasController() según se necesite inyectar
    factory(named("EstudiantesService")) {
        EstudiantesServiceImpl(
            get(named("DataBaseRepository")),
            //getProperty("server_url")
            get(named("EstudiantesCache"))
        )
    }
}