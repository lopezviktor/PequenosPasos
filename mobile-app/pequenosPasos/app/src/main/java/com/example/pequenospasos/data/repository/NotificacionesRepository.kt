package com.example.pequenospasos.data.repository

import com.example.pequenospasos.data.model.Notificacion
import com.example.pequenospasos.data.network.ApiService

class NotificacionesRepository(
    private val apiService: ApiService
) {

    suspend fun getNotificacionesByPadreId(padreId: Long): List<Notificacion> {
        return apiService.getNotificacionesByPadreId(padreId)
    }

    suspend fun marcarNotificacionComoLeida(id: Long) {
        apiService.marcarNotificacionComoLeida(id)
    }

    suspend fun marcarTodasComoLeidas(padreId: Long) {
        apiService.marcarTodasComoLeidas(padreId)
    }

    suspend fun getNumeroNotificacionesNoLeidas(padreId: Long): Int {
        return apiService.getNotificacionesNoLeidas(padreId).size
    }
}