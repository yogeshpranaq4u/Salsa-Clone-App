package com.salsa.clone.salsaclone.ui.search

sealed class SearchEvent {
    data class Search(val query: String) : SearchEvent()
    object LoadCategories : SearchEvent()
}