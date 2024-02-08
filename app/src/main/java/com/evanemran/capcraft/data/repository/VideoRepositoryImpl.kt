package com.evanemran.capcraft.data.repository

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.evanemran.capcraft.domain.model.VideoData
import com.evanemran.capcraft.domain.repository.VideoRepository
import java.security.AccessController.getContext
import java.util.concurrent.TimeUnit


class VideoRepositoryImpl(private val context: Context): VideoRepository {
    override suspend fun getVideoFiles(): List<VideoData> {
        val videos: MutableList<VideoData> = ArrayList<VideoData>()
        val collection = MediaStore.Video.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE
        )

        val selection = MediaStore.Video.Media.DURATION +
                " >= ?"
        val selectionArgs = arrayOf<String>(
            java.lang.String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))
        )
        val sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC"

        context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        ).use { cursor ->
            // Cache column indices.
            val idColumn: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id: Long = cursor.getLong(idColumn)
                val name: String = cursor.getString(nameColumn)
                val duration: Int = cursor.getInt(durationColumn)
                val size: Int = cursor.getInt(sizeColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id
                )

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                videos.add(VideoData(contentUri, name, duration, size, isVideoSelected = false))
            }
        }
        return videos
    }
}