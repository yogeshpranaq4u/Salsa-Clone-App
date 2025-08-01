package com.salsa.clone.salsaclone.data.repository

import com.salsa.clone.salsaclone.data.model.Category
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeSearchRepository @Inject constructor() : SearchRepository {
    override suspend fun getCategories(): List<Category> {
        delay(500)
        return FakeRepository.fakeCategories
    }
}
