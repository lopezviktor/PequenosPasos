package com.example.pequenospasos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.pequenospasos.data.repository.ActividadRepository
import com.example.pequenospasos.data.model.ActividadResponse

class ActividadViewModel(private val actividadRepository: ActividadRepository) : ViewModel() {

    private val _actividades = MutableStateFlow<List<ActividadResponse>>(emptyList())
    val actividades: StateFlow<List<ActividadResponse>> get() = _actividades

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun loadActividades(idNino: Long) {
        viewModelScope.launch {
            try {
                val result = actividadRepository.getActividadesByNinoId(idNino)
                _actividades.value = result
            } catch (e: Exception) {
                _actividades.value = emptyList()
                _errorMessage.value = "Error al cargar actividades"
            }
        }
    }
}