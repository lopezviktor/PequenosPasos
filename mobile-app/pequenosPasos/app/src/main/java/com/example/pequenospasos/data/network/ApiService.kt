package com.example.pequenospasos.data.network

import com.example.pequenospasos.data.model.ActividadResponse
import com.example.pequenospasos.data.model.Conversacion
import com.example.pequenospasos.data.model.Higiene
import com.example.pequenospasos.data.model.LoginRequest
import com.example.pequenospasos.data.model.LoginResponse
import com.example.pequenospasos.data.model.Mensaje
import com.example.pequenospasos.data.model.MensajeResponse
import com.example.pequenospasos.data.model.Nino
import com.example.pequenospasos.data.model.Notificacion
import com.example.pequenospasos.data.model.Padre
import com.pequenospasos.movil.data.model.Comida
import com.pequenospasos.movil.data.model.Siesta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/usuarios/me")
    suspend fun getUsuarioAutenticado(@Header("Authorization") token: String): Response<Padre>

    @GET("api/padres/mis-ninos")
    suspend fun getMisNinos(@Header("Authorization") token: String): Response<List<Nino>>

    // Obtener los niños de un padre
    @GET("api/ninos/padre/{id}")
    suspend fun getNinosByPadreId(@Path("id") id: Long): List<Nino>

    // Obtener las comidas de un niño
    @GET("api/comidas/nino/{id}")
    suspend fun getComidasByNinoId(@Path("id") id: Long): List<Comida>

    // Obtener las siestas de un niño
    @GET("api/siestas/nino/{id}")
    suspend fun getSiestasByNinoId(@Path("id") id: Long): List<Siesta>

    // Obtener los registros de higiene de un niño
    @GET("api/higiene/nino/{id}")
    suspend fun getHigieneByNinoId(@Path("id") id: Long): List<Higiene>

    // Obtener las actividades de un niño
    @GET("api/actividad-ninos/nino/{id}")
    suspend fun getActividadesByNinoId(@Path("id") id: Long): List<ActividadResponse>

    // Endpoint de login
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // Obtener todas las notificaciones de un padre
    @GET("api/notificaciones/receptor/{receptorId}")
    suspend fun getNotificacionesByPadreId(@Path("receptorId") id: Long): List<Notificacion>

    // Marcar una notificación como leída
    @PUT("api/notificaciones/{id}/marcar-leida")
    suspend fun marcarNotificacionComoLeida(@Path("id") id: Long): Response<Unit>

    // Marcar todas como leídas para un padre
    @PUT("api/notificaciones/receptor/{receptorId}/marcar-todas-leidas")
    suspend fun marcarTodasComoLeidas(@Path("receptorId") id: Long): Response<Unit>

    // Obtener las notificaciones no leídas de un padre
    @GET("api/notificaciones/receptor/{receptorId}/no-leidas")
    suspend fun getNotificacionesNoLeidas(@Path("receptorId") id: Long): List<Notificacion>

    // Obtener las conversaciones del padre
    @GET("api/mensajes/usuario/{padreId}")
    suspend fun getConversaciones(@Path("padreId") padreId: Long): List<MensajeResponse>

    // Obtener los mensajes de una conversación
    @GET("api/mensajes/chat/completo")
    suspend fun getConversacionCompleta(
        @Query("usuario1") usuario1Id: Long,
        @Query("usuario2") usuario2Id: Long
    ): List<MensajeResponse>

    // Enviar un nuevo mensaje
    @POST("api/mensajes")
    suspend fun enviarMensaje(@Body mensaje: Mensaje): Response<Mensaje>

}