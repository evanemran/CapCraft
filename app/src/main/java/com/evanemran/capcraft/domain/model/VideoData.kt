package com.evanemran.capcraft.domain.model

import android.net.Uri

data class VideoData(
    val videoUri: Uri,
    val videoName: String,
    val videoDuration: Int,
    val videoSize: Int,
    val isVideoSelected: Boolean
)