package com.moodsense.ai.data.repository

import com.google.gson.annotations.SerializedName

/**
 * Data models for the iTunes Search API
 */
data class ItunesSearchResponse(
    val resultCount: Int,
    val results: List<ItunesTrackDto>
)

data class ItunesTrackDto(
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val trackViewUrl: String?,
    val kind: String?
)
