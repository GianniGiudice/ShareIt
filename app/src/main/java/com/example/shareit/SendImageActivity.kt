package com.example.shareit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SendImageActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendimage);

        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("pictureDirectory")
            Toast.makeText(this, "$value", Toast.LENGTH_LONG).show()
            //The key argument here must match that used in the other activity
        }

    }
}