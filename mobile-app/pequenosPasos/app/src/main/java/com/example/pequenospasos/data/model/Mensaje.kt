package com.example.pequenospasos.data.model

data class Mensaje(
    val id: Long? = null,
    val contenido: String,
    val emisor: Padre,
    val receptor: Educador,
    val estado: String,
    val fecha: String? = null
)