package com.example.pequenospasos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pequenospasos.data.repository.ActividadRepository

class ActividadViewModelFactory(private val repository: ActividadRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActividadViewModel::class.java)) {
            return ActividadViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}