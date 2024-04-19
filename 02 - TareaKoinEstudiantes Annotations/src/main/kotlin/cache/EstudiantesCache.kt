package org.example.cache

import org.example.models.Estudiante
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Property

@Factory
class EstudiantesCache(
    @Property("cache.size")
    _size: String = "5"
) : CacheImpl<Long, Estudiante>(_size.toInt()) {
    val cacheSize: Int = _size.toInt()
}