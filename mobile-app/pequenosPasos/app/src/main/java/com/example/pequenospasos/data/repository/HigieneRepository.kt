package com.example.pequenospasos.data.repository

import com.example.pequenospasos.data.model.Higiene
import com.example.pequenospasos.data.network.RetrofitClient

class HigieneRepository {

    suspend fun getHigienes(ninoId: Long): List<Higiene> {
        return try {
            RetrofitClient.api.getHigieneByNinoId(ninoId)
        } catch (e: Exception) {
            emptyList()
        }
    }
}