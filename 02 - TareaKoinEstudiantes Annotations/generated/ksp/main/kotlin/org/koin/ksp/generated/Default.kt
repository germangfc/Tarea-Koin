package org.koin.ksp.generated

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.*

public fun KoinApplication.defaultModule(): KoinApplication = modules(defaultModule)
public val defaultModule : Module = module {
	single() { org.example.repositories.estudiantes.EstudiantesRepositoryImpl(dbManager=get()) } bind(org.example.repositories.estudiantes.EstudiantesRepository::class)
	single() { org.example.services.database.SqlDeLightManager(getProperty("database.url"),getProperty("database.init.data"),getProperty("database.inmemory")) } 
	factory() { org.example.cache.EstudiantesCache(getProperty("cache.size")) } bind(org.example.cache.CacheImpl::class)
	factory() { org.example.services.estudiantes.EstudiantesServiceImpl(estudiantesRepository=get(),estudiantesCache=get()) } bind(org.example.services.estudiantes.EstudiantesService::class)
}