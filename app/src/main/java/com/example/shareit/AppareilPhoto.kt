package com.example.shareit

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageButton
import androidx.camera.view.PreviewView

class AppareilPhoto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appareilphoto);

        /*val buttonSendView: ImageButton = findViewById(R.id.buttonSendView)
        val buttonViewImage: ImageButton = findViewById(R.id.buttonViewImage)


        buttonSendView.setOnClickListener {
            val intent = Intent(this, SendImageActivity::class.java)
            startActivity(intent)
        }

        buttonViewImage.setOnClickListener {
            val intent = Intent(this, Acceuil::class.java)
            startActivity(intent)
        }*/



        // Set the preferred implementation mode before starting the preview
        previewView.preferredImplementationMode = ImplementationMode.SURFACE_VIEW

// Attach the preview use case and previewView to start the preview
        preview.setSurfaceProvider(previewView.createSurfaceProvider(cameraInfo))

    }
}