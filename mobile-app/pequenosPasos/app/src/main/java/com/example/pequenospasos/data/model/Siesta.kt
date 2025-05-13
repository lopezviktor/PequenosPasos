package com.pequenospasos.movil.data.model

data class Siesta(
    val id: Long,
    val inicioSiesta: String,
    val finSiesta: String?,
    val observaciones: String?,
    val educador: String
)