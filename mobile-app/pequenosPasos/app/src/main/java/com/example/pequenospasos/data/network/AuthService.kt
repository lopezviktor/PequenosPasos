package com.example.pequenospasos.data.network

import android.util.Log
import com.example.pequenospasos.data.model.LoginRequest
import com.example.pequenospasos.data.model.LoginResponse
import retrofit2.Response
import com.auth0.android.jwt.JWT
import android.widget.Toast
import android.content.Context

object AuthService {

    suspend fun login(context: Context, email: String, password: String): Response<LoginResponse> {
        return try {
            val response = RetrofitClient.api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (!token.isNullOrEmpty()) {
                    val tipoUsuario = obtenerTipoUsuarioDesdeToken(token)
                    if (tipoUsuario == "PADRE") {
                        TokenManager.token = token
                        Log.d("AuthService", "Login exitoso. Token guardado: $token")
                    } else {
                        Log.e("AuthService", "Acceso denegado. Rol: $tipoUsuario")
                        Toast.makeText(context, "Esta app es solo para padres.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e("AuthService", "Error: Token vacío o nulo")
                }
            } else {
                Log.e("AuthService", "Error al iniciar sesión: ${response.message()}")
            }
            response
        } catch (e: Exception) {
            Log.e("AuthService", "Error de red: ${e.message}")
            throw e
        }
    }

}

private fun obtenerTipoUsuarioDesdeToken(token: String): String? {
    val jwt = JWT(token)
    return jwt.getClaim("tipoUsuario").asString()
}