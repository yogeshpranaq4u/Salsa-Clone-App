package com.salsa.clone.salsaclone.data.repository

import com.salsa.clone.salsaclone.data.api.CreatorApi
import com.salsa.clone.salsaclone.data.model.Creator

class CreatorRepository(private val api: CreatorApi) {
    suspend fun getCreators(): List<Creator> {
        return api.getCreators().body() ?: emptyList()
    }
}