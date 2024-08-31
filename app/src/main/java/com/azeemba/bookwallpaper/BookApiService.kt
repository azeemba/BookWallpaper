package com.azeemba.bookwallpaper

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes")
    fun searchBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): Call<BookSearchResponse>

    @GET("volumes/{volumeId}")
    fun getVolumeInfo(
        @Path("volumeId") volumeId: String,
        @Query("key") apiKey: String
    ): Call<VolumeInfo>
}

data class BookSearchResponse(
    val items: List<BookSearchItem>
)

data class BookSearchItem(
    val id: String,
    val volumeInfo: VolumeInfoBasic
)

data class VolumeInfoBasic(
    val title: String,
    val authors: List<String>?,
    val imageLinks: ImageLinks?
)

data class VolumeInfo(
    val id: String,
    val volumeInfo: VolumeInfoDetailed
)

data class VolumeInfoDetailed(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String?,
    val small: String?,
    val medium: String?,
    val large: String?,
    val extraLarge: String?
)