package com.example.pequenospasos.data.model

import com.pequenospasos.movil.data.model.Comida
import com.pequenospasos.movil.data.model.Siesta

data class HabitoResumen(
    val comida: Comida,
    val higiene: Higiene,
    val siesta: Siesta,
    val fecha: String
)