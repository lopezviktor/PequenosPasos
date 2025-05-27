package com.example.pequenospasos.data.model

data class MensajeResponse(
    val id: Long,
    val contenido: String,
    val fechaHora: String,
    val estado: String,
    val emisorId: Long,
    val emisorNombre: String,
    val receptorId: Long,
    val receptorNombre: String
)