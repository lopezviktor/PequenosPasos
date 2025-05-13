package com.example.pequenospasos.data.repository

import com.example.pequenospasos.data.network.RetrofitClient
import com.pequenospasos.movil.data.model.Comida

class ComidaRepository {

    suspend fun getComidas(ninoId: Long): List<Comida> {
        return try {
            RetrofitClient.api.getComidasByNinoId(ninoId)
        } catch (e: Exception) {
            emptyList()
        }
    }
}