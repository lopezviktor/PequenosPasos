package com.example.pequenospasos.data.repository

import com.example.pequenospasos.data.network.RetrofitClient
import com.pequenospasos.movil.data.model.Siesta

class SiestaRepository {

    suspend fun getSiestas(ninoId: Long): List<Siesta> {
        return try {
            RetrofitClient.api.getSiestasByNinoId(ninoId)
        } catch (e: Exception) {
            emptyList()
        }
    }
}