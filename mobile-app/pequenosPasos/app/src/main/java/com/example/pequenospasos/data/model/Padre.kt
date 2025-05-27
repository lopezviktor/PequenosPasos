package com.example.pequenospasos.data.model

data class Padre(
    val id: Long,
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val password: String = "",
    val telefono: String = "",
    val tipoUsuario: String = "PADRE",
    val hijos: List<Nino> = emptyList()
)