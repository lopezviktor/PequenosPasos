package com.example.pequenospasos.data.model

data class Padre(
    val tipoUsuario: String,
    val id: Long,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String,
    val telefono: String,
    val hijos: List<Nino>
)