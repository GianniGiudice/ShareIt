package com.example.shareit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.shareit.activities.SendImageActivity
import com.squareup.picasso.Picasso

class ImageDisplayActivity : AppCompatActivity() {
    private lateinit var mImageView: ImageView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display_activity)
        mImageView = findViewById(R.id.image_display)
        val imageview_url = intent.getStringExtra("EXTRA_IMAGEVIEW_URL")
        val imageview_title = intent.getStringExtra("EXTRA_IMAGEVIEW_TITLE")
        val imageview_description = intent.getStringExtra("EXTRA_IMAGEVIEW_DESCRIPTION")
        Picasso.get().load(imageview_url).into(mImageView)

        val button = findViewById<Button>(R.id.button);
        button.setOnClickListener {
           val intent = Intent(this, SendImageActivity::class.java)
            intent.putExtra("EXTRA_IMAGEVIEW_URL", imageview_url)
            intent.putExtra("EXTRA_IMAGEVIEW_TITLE", imageview_title)
            intent.putExtra("EXTRA_IMAGEVIEW_DESCRIPTION", imageview_description)
            startActivity(intent)
        }

    }
}