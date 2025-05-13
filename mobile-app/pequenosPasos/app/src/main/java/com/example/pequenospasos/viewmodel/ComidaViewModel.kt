package com.example.pequenospasos.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenospasos.data.repository.ComidaRepository
import com.example.pequenospasos.util.esHoy
import com.pequenospasos.movil.data.model.Comida
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class ComidaViewModel : ViewModel() {
    private val repository = ComidaRepository()
    private val _comidas = MutableStateFlow<List<Comida>>(emptyList())
    val comidas: StateFlow<List<Comida>> get() = _comidas

    @RequiresApi(Build.VERSION_CODES.O)
    val comidasDeHoy: StateFlow<List<Comida>> =
        comidas.map { lista -> lista.filter { esHoy(it.horaComida) } }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadComidas(ninoId: Long) {
        viewModelScope.launch {
            try {
                val result = repository.getComidas(ninoId)
                _comidas.value = result
                Log.d("ComidaViewModel", "Comidas cargadas: ${result.size}")
                result.forEach { Log.d("Comida", it.toString()) }
            } catch (e: Exception) {
                Log.e("ComidaViewModel", "Error al cargar comidas", e)
            }
        }
    }
}