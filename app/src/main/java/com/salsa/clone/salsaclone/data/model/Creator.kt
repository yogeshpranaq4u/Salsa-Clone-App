package com.salsa.clone.salsaclone.data.model

data class Creator(
    val id: Int,
    val name: String,
    val profileImage: String,
    val thumbImage: String,
    val views: Int,
    val coins: Int
)

data class Category(
    val id: Int,
    val title: String,
    val creators: List<Creator>
)