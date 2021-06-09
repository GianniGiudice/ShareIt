package com.example.shareit.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shareit.*
import com.example.shareit.databinding.SendImageActivityBinding
import com.example.shareit.utils.activityViewBinding
import com.google.common.io.Files.getFileExtension
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image.*

class SendImageActivity : AppCompatActivity(){
    private val binding by activityViewBinding(SendImageActivityBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendimage);

        val adapter =
            ListFriendAdapter(
                listOf("Gianni", "Youssef", "Johann"),
                intent.getStringExtra("EXTRA_IMAGEVIEW_URL")!!,
                intent.getStringExtra("EXTRA_IMAGEVIEW_TITLE")!!,
                intent.getStringExtra("EXTRA_IMAGEVIEW_DESCRIPTION")!!
            )

        binding.recyclerView.adapter = adapter



        /*val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("pictureDirectory")
            Toast.makeText(this, "$value", Toast.LENGTH_LONG).show()
            //The key argument here must match that used in the other activity
        }*/

    }

}