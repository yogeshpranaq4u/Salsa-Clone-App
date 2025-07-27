package com.salsa.clone.salsaclone.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salsa.clone.salsaclone.data.model.Category
import com.salsa.clone.salsaclone.data.repository.FakeRepository
import com.salsa.clone.salsaclone.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// State holder
data class SearchState(
    val query: String = "",
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    init {
        onEvent(SearchEvent.LoadCategories)
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.LoadCategories -> {
                loadCategories()
            }
            is SearchEvent.Search -> {
                search(event.query)
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val categories = repository.getCategories()
                state = state.copy(categories = categories, isLoading = false)
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, query = query, error = null)
            try {
                val allCategories = repository.getCategories()
                val filtered = if (query.isBlank()) {
                    allCategories
                } else {
                    allCategories.map { category ->
                        category.copy(
                            creators = category.creators.filter {
                                it.name.contains(query, ignoreCase = true)
                            }
                        )
                    }.filter { it.creators.isNotEmpty() }
                }
                state = state.copy(categories = filtered, isLoading = false)
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }
}

