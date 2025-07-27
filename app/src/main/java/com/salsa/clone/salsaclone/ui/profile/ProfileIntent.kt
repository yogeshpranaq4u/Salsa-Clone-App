package com.salsa.clone.salsaclone.ui.profile

import android.net.Uri

sealed class ProfileIntent {
    object PickGalleryImage : ProfileIntent()
    data class ImagePicked(val uri: Uri) : ProfileIntent()
}