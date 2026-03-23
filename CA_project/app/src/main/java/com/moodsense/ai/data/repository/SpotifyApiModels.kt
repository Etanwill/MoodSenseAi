package com.moodsense.ai.data.repository

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

// ============================================================
// Spotify API Data Models
// ============================================================

data class SpotifyTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int
)

data class SpotifySearchResponse(
    val tracks: SpotifyTracksWrapper?
)

data class SpotifyTracksWrapper(
    val items: List<SpotifyTrackDto>?,
    val total: Int
)

data class SpotifyTrackDto(
    val id: String,
    val name: String,
    val artists: List<SpotifyArtistDto>,
    val album: SpotifyAlbumDto,
    @SerializedName("external_urls") val externalUrls: SpotifyExternalUrls,
    @SerializedName("preview_url") val previewUrl: String?,
    val uri: String
)

data class SpotifyArtistDto(
    val id: String,
    val name: String
)

data class SpotifyAlbumDto(
    val id: String,
    val name: String,
    val images: List<SpotifyImageDto>
)

data class SpotifyImageDto(
    val url: String,
    val height: Int?,
    val width: Int?
)

data class SpotifyExternalUrls(
    val spotify: String
)

// ============================================================
// Retrofit API Interfaces
// ============================================================

/**
 * Spotify Accounts API for authentication.
 * Uses the standard Authorization: Basic header.
 */
interface SpotifyAuthApi {
    @FormUrlEncoded
    @POST("token")
    suspend fun getAccessToken(
        @Header("Authorization") authHeader: String,
        @Field("grant_type") grantType: String = "client_credentials"
    ): SpotifyTokenResponse
}

/**
 * Spotify Web API for search and track data
 */
interface SpotifyApi {
    @GET("search")
    suspend fun searchTracks(
        @Header("Authorization") authHeader: String,
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 20,
        @Query("market") market: String = "US" // Adding market can help stabilize 403 issues
    ): SpotifySearchResponse
}
