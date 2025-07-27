package com.salsa.clone.salsaclone.ui.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.ImagePicked -> {
                _state.update { it.copy(avatarUri = intent.uri) }
                // Possibly persist URI if needed
            }
            is ProfileIntent.PickGalleryImage -> {
                // Trigger picking logic if needed
            }
        }
    }
}