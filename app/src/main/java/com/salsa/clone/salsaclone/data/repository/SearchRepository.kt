package com.salsa.clone.salsaclone.data.repository

import com.salsa.clone.salsaclone.data.model.Category

interface SearchRepository {
    suspend fun getCategories(): List<Category>
}