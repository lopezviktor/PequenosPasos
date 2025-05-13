package com.example.pequenospasos.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenospasos.data.repository.SiestaRepository
import com.pequenospasos.movil.data.model.Siesta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.pequenospasos.util.esHoy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class SiestaViewModel : ViewModel() {

    private val repository = SiestaRepository()

    private val _siestas = MutableStateFlow<List<Siesta>>(emptyList())
    val siestas: StateFlow<List<Siesta>> = _siestas

    @RequiresApi(Build.VERSION_CODES.O)
    val siestasDeHoy: StateFlow<List<Siesta>> =
        siestas.map { lista -> lista.filter { esHoy(it.inicioSiesta) } }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadSiestas(ninoId: Long) {
        viewModelScope.launch {
            val data = repository.getSiestas(ninoId)
            _siestas.value = data
        }
    }
}