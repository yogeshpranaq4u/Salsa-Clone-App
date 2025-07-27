package com.salsa.clone.salsaclone.data.api

import com.salsa.clone.salsaclone.data.model.Category
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPi {
    @GET("categories")
    suspend fun getCategories(@Query("search") query: String): List<Category>
}