package com.azeemba.bookwallpaper

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var bookTitle: EditText
    private lateinit var bookAuthor: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bookTitle = findViewById(R.id.bookTitle)
        bookAuthor = findViewById(R.id.bookAuthor)
        searchButton = findViewById(R.id.searchButton)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        bookAdapter = BookAdapter()
        recyclerView.adapter = bookAdapter

        searchButton.setOnClickListener {
            val title = bookTitle.text.toString()
            val author = bookAuthor.text.toString()
            val query = "intitle:$title+inauthor:$author"

            ApiServiceProvider.bookApiService.searchBooks(query, ApiServiceProvider.API_KEY).enqueue(object : Callback<BookSearchResponse> {
                override fun onResponse(call: Call<BookSearchResponse>, response: Response<BookSearchResponse>) {
                    if (response.isSuccessful) {
                        val books = response.body()?.items ?: emptyList()
                        bookAdapter.submitList(books)
                    }
                }

                override fun onFailure(call: Call<BookSearchResponse>, t: Throwable) {
                    // Handle error
                }
            })
        }
    }
}