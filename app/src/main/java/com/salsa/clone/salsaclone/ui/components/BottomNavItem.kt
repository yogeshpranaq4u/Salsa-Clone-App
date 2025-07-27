package com.salsa.clone.salsaclone.ui.components

import com.salsa.clone.salsaclone.R

sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object ForYou : BottomNavItem("for_you", R.drawable.for_you, "For you")
    object Search : BottomNavItem("search", R.drawable.search_icon, "Search")
    object Chat : BottomNavItem("chat", R.drawable.chat_icon, "Chat")
    object Match : BottomNavItem("match", R.drawable.heart_icon, "Match")
    object Profile : BottomNavItem("profile", R.drawable.profile_icon, "Profile")
}
