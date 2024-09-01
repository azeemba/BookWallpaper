package com.azeemba.bookwallpaper

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
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

    private lateinit var progressBarr: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bookTitle = findViewById(R.id.bookTitle)
        bookAuthor = findViewById(R.id.bookAuthor)
        searchButton = findViewById(R.id.searchButton)
        recyclerView = findViewById(R.id.recyclerView)
        progressBarr = findViewById(R.id.progressBar_cyclic)

        recyclerView.layoutManager = LinearLayoutManager(this)

        bookAdapter = BookAdapter()
        recyclerView.adapter = bookAdapter

        searchButton.setOnClickListener {
            progressBarr.visibility = View.VISIBLE
            val title = bookTitle.text.toString()
            val author = bookAuthor.text.toString()
            val query = "intitle:$title+inauthor:$author"

            ApiServiceProvider.bookApiService.searchBooks(query, ApiServiceProvider.API_KEY).enqueue(object : Callback<BookSearchResponse> {
                override fun onResponse(call: Call<BookSearchResponse>, response: Response<BookSearchResponse>) {
                    if (response.isSuccessful) {
                        val books = response.body()?.items ?: emptyList()
                        bookAdapter.submitList(books)
                    }
                    progressBarr.visibility = View.GONE
                }

                override fun onFailure(call: Call<BookSearchResponse>, t: Throwable) {
                    // Handle error
                    progressBarr.visibility = View.GONE
                }
            })
        }
    }
}