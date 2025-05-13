package com.example.pequenospasos.data.model

data class Higiene(
    val id: Long,
    val fechaHora: String,
    val estado: String,
    val observaciones: String?,
    val educador: String
)