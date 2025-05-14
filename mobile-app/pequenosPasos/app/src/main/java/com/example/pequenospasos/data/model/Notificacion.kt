package com.example.pequenospasos.data.model

data class Notificacion(
    val id: Long,
    val mensaje: String,
    val fechaHora: String,
    val leida: Boolean,
    val estado: String
)