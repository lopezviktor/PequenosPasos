package com.pequenospasos.movil.data.model

data class Comida(
    val id: Long,
    val horaComida: String,
    val descripcionComida: String,
    val observaciones: String?,
    val educador: String
)