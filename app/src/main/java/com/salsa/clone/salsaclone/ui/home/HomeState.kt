package com.salsa.clone.salsaclone.ui.home

import com.salsa.clone.salsaclone.data.model.Creator

data class HomeState(
    val isLoading: Boolean = false,
    val creators: List<Creator> = emptyList(),
    val error: String? = null
)
