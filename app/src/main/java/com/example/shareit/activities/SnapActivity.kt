package com.example.shareit.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shareit.Constants
import com.example.shareit.R
import com.example.shareit.databinding.ActivitySnapBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SnapActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnapBinding
    private var imageCapture:ImageCapture?=null
    private lateinit var outputDirectory: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivitySnapBinding.inflate(layoutInflater)
        setContentView(binding.root);

        outputDirectory = outputDirectory()

        if(allPermissionGranted()){
            startCamera()
        }
        else{
            ActivityCompat.requestPermissions(
                this, Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSIONS
            )
        }
        binding.btnTakePhoto.setOnClickListener{
            takePhoto()
            //Toast.makeText(this@AppareilPhoto, "$outputDirectory", Toast.LENGTH_LONG).show()
            val intent = Intent(this, SendImageActivity::class.java)
            //intent.putExtra("pictureDirectory", pictureDirectory);
            startActivity(intent)
        }
    }

    private fun outputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun takePhoto(){
        val imageCapture = imageCapture?:return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                Constants.FILE_NAME_FORMAT,
                Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg")

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "photo saved"
                    Toast.makeText(this@SnapActivity, "$msg $savedUri", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(Constants.TAG, "onError mdmdml: ${exception.message}", exception)
                }

            }

        )

    }

    private fun startCamera(){
        val CameraProviderFuture = ProcessCameraProvider.getInstance(this)

        CameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = CameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                mPreview->mPreview.setSurfaceProvider(
                    binding.viewFinder.surfaceProvider
                )
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    preview, imageCapture
                )
            }catch (e:Exception){
                Log.d(Constants.TAG, "Start camera fail:", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(requestCode == Constants.REQUEST_CODE_PERMISSIONS){
            if(allPermissionGranted()){
                startCamera()
            }else{
                Toast.makeText(this,"Permission not allow by the user",Toast.LENGTH_SHORT).show()
                finish()

            }

        }

    }

    private fun allPermissionGranted() =
        Constants.REQUIRED_PERMISSIONS.all{
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }

}













