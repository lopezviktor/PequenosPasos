package com.example.pequenospasos.viewmodel

import com.squareup.moshi.JsonDataException

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenospasos.data.model.LoginRequest
import com.example.pequenospasos.data.model.Padre
import com.example.pequenospasos.data.model.Nino
import com.example.pequenospasos.data.network.RetrofitClient
import com.example.pequenospasos.data.network.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginResult = MutableStateFlow<String>("")
    val loginResult: StateFlow<String> get() = _loginResult

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    private val _padre = MutableStateFlow<Padre?>(null)
    val padre: StateFlow<Padre?> get() = _padre

    private val _hijos = MutableStateFlow<List<Nino>>(emptyList())
    val hijos: StateFlow<List<Nino>> get() = _hijos

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val token = response.body()?.token ?: ""
                    TokenManager.token = token
                    _loginResult.value = "Login exitoso: Token guardado"

                    val tokenConBearer = "Bearer $token"
                    val userResponse = RetrofitClient.api.getUsuarioAutenticado(tokenConBearer)
                    if (userResponse.isSuccessful) {
                        val padre = userResponse.body()
                        if (padre != null) {
                            Log.d("LoginViewModel", "Padre: $padre")
                            Log.d("LoginViewModel", "ID: ${padre.id}")
                            Log.d("LoginViewModel", "Nombre: ${padre.nombre}")
                            Log.d("LoginViewModel", "Apellidos: ${padre.apellidos}")
                            Log.d("LoginViewModel", "Email: ${padre.email}")
                            Log.d("LoginViewModel", "Teléfono: ${padre.telefono}")
                            Log.d("LoginViewModel", "Tipo de usuario: ${padre.tipoUsuario}")
                            _padre.value = padre

                            // Obtener los niños del padre autenticado
                            try {
                                val ninosResponse = RetrofitClient.api.getMisNinos(tokenConBearer)
                                if (ninosResponse.isSuccessful) {
                                    val ninos = ninosResponse.body()
                                    if (ninos != null) {
                                        Log.d("LoginViewModel", "Niños del padre: $ninos")
                                        _hijos.value = ninos
                                        Log.d("NINOS_RESPONSE", ninos.toString())
                                    } else {
                                        Log.e("LoginViewModel", "Lista de niños es null")
                                    }
                                } else {
                                    Log.e("LoginViewModel", "Error al obtener niños: ${ninosResponse.message()}")
                                }
                            } catch (e: Exception) {
                                when (e) {
                                    is JsonDataException -> Log.e("LoginViewModel", "Error de mapeo JSON en los niños", e)
                                    else -> Log.e("LoginViewModel", "Excepción al obtener niños", e)
                                }
                            }
                        } else {
                            Log.e("LoginViewModel", "El objeto Padre es null en la respuesta del endpoint /me")
                        }
                    } else {
                        Log.e("LoginViewModel", "Error al obtener el padre con /me: ${userResponse.message()}")
                    }

                    _isLoggedIn.value = true
                    Log.d("LoginViewModel", "Token: $token")

                } else {
                    _loginResult.value = "Error: ${response.message()}"
                    _isLoggedIn.value = false
                }
            } catch (e: Exception) {
                _loginResult.value = "Error de conexión: ${e.message}"
                _isLoggedIn.value = false
                Log.e("LoginViewModel", "Error al iniciar sesión", e)
            }
        }
    }
}