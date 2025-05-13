package com.example.pequenospasos.data.model

data class Nino(
    val id: Long,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String,
    val primerDia: String,
    val alergias: String?,
    val condicionesMedicas: String?,
    val fotoUrl: String?,
    val clase: Clase
)