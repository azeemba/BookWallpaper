package com.azeemba.bookwallpaper

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceProvider {
    const val API_KEY = "AIzaSyDMjPfVWXtHIppjziHZKbrb9Ux6O8PnKkE"
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    val bookApiService: BookApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(BookApiService::class.java)
    }
}