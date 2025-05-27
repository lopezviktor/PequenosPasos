package com.example.pequenospasos.data.model

data class Educador(
    val id: Long,
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val password: String = "",
    val telefono: String = "",
    val tipoUsuario: String = "EDUCADOR"
)