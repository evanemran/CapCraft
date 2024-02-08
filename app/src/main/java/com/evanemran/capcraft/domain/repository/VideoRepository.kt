package com.evanemran.capcraft.domain.repository

import com.evanemran.capcraft.domain.model.VideoData

interface VideoRepository {
    suspend fun getVideoFiles(): List<VideoData>
}