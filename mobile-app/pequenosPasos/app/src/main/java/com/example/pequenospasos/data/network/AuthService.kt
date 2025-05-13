package com.example.pequenospasos.data.network

import android.util.Log
import com.example.pequenospasos.data.model.LoginRequest
import com.example.pequenospasos.data.model.LoginResponse
import retrofit2.Response

object AuthService {

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return try {
            val response = RetrofitClient.api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (!token.isNullOrEmpty()) {
                    TokenManager.token = token
                    Log.d("AuthService", "Login exitoso. Token guardado: $token")
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