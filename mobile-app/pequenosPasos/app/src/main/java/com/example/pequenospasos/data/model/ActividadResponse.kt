package com.example.pequenospasos.data.model

data class ActividadResponse(
    val id: Long,
    val nino: NinoResponse,
    val actividad: ActividadDetalle,
    val fechaRegistro: String,
    val educador: EducadorResponse
)

data class ActividadDetalle(
    val actividadId: Long,
    val nombre: String,
    val descripcion: String
)

data class NinoResponse(
    val id: Long,
    val nombre: String,
    val apellidos: String,
    val clase: ClaseResponse
)

data class ClaseResponse(
    val id: Long,
    val nombre: String,
    val educador: EducadorResponse
)

data class EducadorResponse(
    val id: Long,
    val nombre: String,
    val apellidos: String
)