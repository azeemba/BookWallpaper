package com.azeemba.bookwallpaper

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

class WallpaperPreviewActivity : AppCompatActivity() {

    private lateinit var previewImageView: ImageView
    private lateinit var confirmButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper_preview)

        previewImageView = findViewById(R.id.previewImageView)
        confirmButton = findViewById(R.id.confirmButton)
        progressBar = findViewById(R.id.progressBar_wp)

        val bookId = intent.getStringExtra("BOOK_ID")
        val bookTitle = intent.getStringExtra("BOOK_TITLE")
        var imageUrl = ""

        if (bookId != null) {
            progressBar.visibility = View.VISIBLE
            ApiServiceProvider.bookApiService.getVolumeInfo(bookId, ApiServiceProvider.API_KEY).enqueue(object: Callback<VolumeInfo>{
                override fun onResponse(call: Call<VolumeInfo>, response: Response<VolumeInfo>) {
                    if (response.isSuccessful) {
                        val volume = response.body()?.volumeInfo;
                        val imageUrl = volume?.imageLinks?.let {
                            (it.extraLarge ?: it.large ?: it.medium ?: it.small ?: it.thumbnail)?.replace("http://", "https://")
                        }

                        Glide.with(baseContext)
                            .load(imageUrl)
                            .listener(object: RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    progressBar.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    progressBar.visibility = View.GONE
                                    return false
                                }
                            })
                            .into(previewImageView)

                        confirmButton.setOnClickListener {
                            setWallpaper(imageUrl)
                        }
                    }
                }

                override fun onFailure(call: Call<VolumeInfo>, t: Throwable) {
                }
            })
        }

        title = "Set wallpaper: $bookTitle"
    }

    private fun setWallpaper(imageUrl: String?) {
        progressBar.visibility = View.VISIBLE
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    try {
                        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                        wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK)
                        wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_SYSTEM)
                        Toast.makeText(this@WallpaperPreviewActivity, "Wallpaper set successfully", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                        finish()
                        moveTaskToBack(true)
                    } catch (e: Exception) {
                        Toast.makeText(this@WallpaperPreviewActivity, "Failed to set wallpaper", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }
                }
            })
    }
}
