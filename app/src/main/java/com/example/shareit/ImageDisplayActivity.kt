package com.example.shareit

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ImageDisplayActivity : AppCompatActivity() {
    private lateinit var mImageView: ImageView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display_activity)
        mImageView = findViewById(R.id.image_display)
        val imageview_url = intent.getStringExtra("EXTRA_IMAGEVIEW_URL")
        Picasso.get().load(imageview_url).into(mImageView)
    }
}