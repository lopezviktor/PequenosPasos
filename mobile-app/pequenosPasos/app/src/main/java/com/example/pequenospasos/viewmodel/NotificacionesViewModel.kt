package com.example.pequenospasos.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenospasos.data.model.Notificacion
import com.example.pequenospasos.data.repository.NotificacionesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificacionesViewModel(
    private val repository: NotificacionesRepository
) : ViewModel() {

    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones

    fun cargarNotificaciones(padreId: Long) {
        viewModelScope.launch {
            try {
                val lista = repository.getNotificacionesByPadreId(padreId)
                _notificaciones.value = lista
                Log.d("NOTIFICACIONES_DEBUG", "Cargadas ${lista.size} notificaciones")
            } catch (e: Exception) {
                // Log error si quieres
                _notificaciones.value = emptyList()
            }
        }
    }

    fun marcarComoLeida(id: Long) {
        viewModelScope.launch {
            try {
                repository.marcarNotificacionComoLeida(id)
                _notificaciones.value = _notificaciones.value.map {
                    if (it.id == id) it.copy(estado = "LEIDO") else it
                }
            } catch (_: Exception) {}
        }
    }

    fun marcarTodasComoLeidas(padreId: Long) {
        viewModelScope.launch {
            try {
                repository.marcarTodasComoLeidas(padreId)
                _notificaciones.value = _notificaciones.value.map {
                    it.copy(estado = "LEIDO")
                }
            } catch (_: Exception) {}
        }
    }
}