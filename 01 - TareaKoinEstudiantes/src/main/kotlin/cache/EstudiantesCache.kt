package org.example.cache

import org.example.models.Estudiante

class EstudiantesCache(size:Int) : CacheImpl<Long,Estudiante>(size)