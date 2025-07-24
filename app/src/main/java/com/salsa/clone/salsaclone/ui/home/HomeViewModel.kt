package com.salsa.clone.salsaclone.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salsa.clone.salsaclone.data.repository.CreatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CreatorRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init {
        onEvent(HomeEvent.LoadCreators)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadCreators -> {
                viewModelScope.launch {
                    state = state.copy(isLoading = true)
                    try {
                        val creators = repository.getCreators()
                        state = state.copy(creators = creators, isLoading = false)
                    } catch (e: Exception) {
                        state = state.copy(error = e.message, isLoading = false)
                    }
                }
            }
        }
    }
}