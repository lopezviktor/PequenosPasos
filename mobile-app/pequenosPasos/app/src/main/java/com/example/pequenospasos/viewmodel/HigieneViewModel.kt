package com.example.pequenospasos.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenospasos.data.model.Higiene
import com.example.pequenospasos.data.repository.HigieneRepository
import com.example.pequenospasos.util.esHoy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class HigieneViewModel : ViewModel() {

    private val repository = HigieneRepository()

    private val _higienes = MutableStateFlow<List<Higiene>>(emptyList())
    val higienes: StateFlow<List<Higiene>> = _higienes

    @RequiresApi(Build.VERSION_CODES.O)
    val higienesDeHoy: StateFlow<List<Higiene>> =
        higienes.map { lista -> lista.filter { esHoy(it.fechaHora) } }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadHigienes(ninoId: Long) {
        viewModelScope.launch {
            val data = repository.getHigienes(ninoId)
            _higienes.value = data
        }
    }
}