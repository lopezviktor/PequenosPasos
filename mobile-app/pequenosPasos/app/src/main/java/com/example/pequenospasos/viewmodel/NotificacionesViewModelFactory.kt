package com.example.pequenospasos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pequenospasos.data.repository.NotificacionesRepository

class NotificacionesViewModelFactory(
    private val repository: NotificacionesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificacionesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificacionesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}