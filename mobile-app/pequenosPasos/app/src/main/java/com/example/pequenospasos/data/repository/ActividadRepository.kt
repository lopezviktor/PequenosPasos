package com.example.pequenospasos.data.repository

import com.example.pequenospasos.data.model.ActividadResponse
import com.example.pequenospasos.data.network.ApiService

class ActividadRepository(private val apiService: ApiService) {

    suspend fun getActividadesByNinoId(id: Long): List<ActividadResponse> {
        return try {
            apiService.getActividadesByNinoId(id)
        } catch (e: Exception) {
            emptyList()
        }
    }
}