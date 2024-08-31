package com.azeemba.bookwallpaper

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter() : ListAdapter<BookSearchItem, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.bookTitleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.bookAuthorTextView)
        private val coverImageView: ImageView = itemView.findViewById(R.id.bookCoverImageView)

        fun bind(book: BookSearchItem) {
            titleTextView.text = book.volumeInfo.title
            authorTextView.text = book.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author"

            val image = book.volumeInfo.imageLinks?.let {
                (it.extraLarge ?: it.large ?: it.medium ?: it.small ?: it.thumbnail)?.replace("http://", "https://")
            }

            Glide.with(itemView.context)
                .load(image)
                .placeholder(R.drawable.book_placeholder)
                .into(coverImageView)
            Log.i("VIEW", "Was there an image?: " + book.volumeInfo.imageLinks?.extraLarge);
            Log.i("VIEW", "Was there an image?: " + book.volumeInfo.imageLinks?.large);
            Log.i("VIEW", "Was there an image?: " + book.volumeInfo.imageLinks?.medium);
            Log.i("VIEW", "Was there an image?: " + book.volumeInfo.imageLinks?.small);
            Log.i("VIEW", "Was there an image?: " + book.volumeInfo.imageLinks?.thumbnail);

            // Set click listener
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, WallpaperPreviewActivity::class.java).apply {
                    putExtra("BOOK_ID", book.id)
                    putExtra("BOOK_TITLE", book.volumeInfo.title)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<BookSearchItem>() {
        override fun areItemsTheSame(oldItem: BookSearchItem, newItem: BookSearchItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BookSearchItem, newItem: BookSearchItem): Boolean {
            return oldItem == newItem
        }
    }
}