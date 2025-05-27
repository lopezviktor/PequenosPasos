package com.example.pequenospasos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenospasos.data.model.Educador
import com.example.pequenospasos.data.model.Mensaje
import com.example.pequenospasos.data.model.MensajeResponse
import com.example.pequenospasos.data.network.RetrofitClient
import kotlinx.coroutines.launch

class MensajesViewModel : ViewModel() {

    private val apiService = RetrofitClient.api

    // Lista de todos los mensajes
    private val _mensajes = MutableLiveData<List<MensajeResponse>>()
    val mensajes: LiveData<List<MensajeResponse>> = _mensajes

    // Lista de educadores con los que el padre tiene mensajes (conversaciones abiertas)
    private val _educadores = MutableLiveData<List<Educador>>()
    val educadores: LiveData<List<Educador>> = _educadores

    private val _nuevoMensaje = MutableLiveData<Mensaje?>()
    val nuevoMensaje: LiveData<Mensaje?> = _nuevoMensaje

    /**
     * Carga todos los mensajes del padre y extrae los educadores únicos con los que tiene conversación.
     */
    fun cargarEducadoresConConversacion(padreId: Long) {
        viewModelScope.launch {
            try {
                // Obtener todos los mensajes del padre (en formato MensajeResponse)
                val mensajesResponse = apiService.getConversaciones(padreId)

                _mensajes.value = mensajesResponse

                // Procesar para obtener lista de educadores únicos
                val educadoresUnicos = mensajesResponse
                    .mapNotNull { mensaje ->
                        when (padreId) {
                            mensaje.emisorId -> Educador(
                                id = mensaje.receptorId,
                                nombre = mensaje.receptorNombre,
                                apellidos = "",
                                email = "",
                                telefono = "",
                                password = "",
                                tipoUsuario = "EDUCADOR"
                            )
                            mensaje.receptorId -> Educador(
                                id = mensaje.emisorId,
                                nombre = mensaje.emisorNombre,
                                apellidos = "",
                                email = "",
                                telefono = "",
                                password = "",
                                tipoUsuario = "EDUCADOR"
                            )
                            else -> null
                        }
                    }
                    .distinctBy { it.id }

                _educadores.value = educadoresUnicos

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Carga todos los mensajes entre el padre y un educador específico.
     */
    fun cargarMensajes(usuario1Id: Long, usuario2Id: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.getConversacionCompleta(usuario1Id, usuario2Id)
                _mensajes.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun enviarMensaje(mensaje: Mensaje) {
        viewModelScope.launch {
            try {
                val response = apiService.enviarMensaje(mensaje)
                if (response.isSuccessful) {
                    _nuevoMensaje.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}