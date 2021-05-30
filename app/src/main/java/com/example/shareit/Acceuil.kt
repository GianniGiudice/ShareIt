package com.example.shareit


import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Acceuil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        val sendImage: ImageButton = findViewById(R.id.buttonSendView)


        sendImage.setOnClickListener {
            val intent = Intent(this, AppareilPhoto::class.java)
            startActivity(intent)
            //setContentView(R.layout.activity_appareilphoto);
            //Toast.makeText(this, "change view", Toast.LENGTH_SHORT).show()
        }
    }



}