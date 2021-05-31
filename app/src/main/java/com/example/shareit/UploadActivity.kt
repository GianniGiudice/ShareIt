package com.example.shareit

import android.content.Intent
import android.net.Uri
import android.net.Uri.EMPTY
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

class UploadActivity : AppCompatActivity() {
    private lateinit var mStorageRef: StorageReference
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mButtonChooseImage: Button
    private lateinit var mButtonUpload: Button
    private lateinit var mEditTextFileName: EditText
    private lateinit var mImageView: ImageView
    private var mImageUri: Uri = EMPTY
    private lateinit var mEditTextFileDescription: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload)
        mButtonChooseImage = findViewById(R.id.button_choose_image)
        mButtonUpload = findViewById(R.id.button_upload)
        mEditTextFileName = findViewById(R.id.edit_text_file_name)
        mImageView = findViewById(R.id.image_view)
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads")
        mButtonChooseImage.setOnClickListener { openFileChooser() }
        mButtonUpload.setOnClickListener {
            /*if (mUploadTask != null && mUploadTask!!.isInProgress) {
                Toast.makeText(this@MainActivity, "Upload already in progress", Toast.LENGTH_SHORT).show()
            } else {*/
            uploadFile()
            //}
        }
        mEditTextFileDescription = findViewById(R.id.edit_text_file_description)
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data!!
            Picasso.get().load(mImageUri).into(mImageView)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadFile() {
        if (mImageUri != EMPTY) {
            val fileReference = mStorageRef.child(System.currentTimeMillis()
                .toString() + "." + getFileExtension(mImageUri))
            val mUploadTask : UploadTask = fileReference.putFile(mImageUri)
            mUploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                fileReference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    println("Upload $downloadUri")
                    Toast.makeText(this@UploadActivity, R.string.upload_success, Toast.LENGTH_SHORT).show()
                    if (downloadUri != null) {
                        val upload = Upload(mEditTextFileName.text.toString().trim { it <= ' ' }, downloadUri.toString(),mEditTextFileDescription.text.toString().trim { it <= ' ' })
                        val key = mDatabaseRef.push().key
                        if (key != null) {
                            mDatabaseRef.child(key).setValue(upload)
                        } else {
                            mDatabaseRef.setValue(upload)
                        }
                    }
                    mEditTextFileName.text = null
                    mEditTextFileDescription.text = null
                    mImageView.setImageResource(android.R.color.transparent)
                    mImageUri = EMPTY
                    mEditTextFileName.requestFocus()
                } else {
                    Toast.makeText(this@UploadActivity, R.string.upload_fail, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, R.string.no_file, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.activity1 -> {
                val intent = Intent(this, HomeActivity::class.java)
                this.startActivity(intent)
            }
            R.id.activity2 -> {
                val intent = Intent(this, UploadActivity::class.java)
                this.startActivity(intent)
            }
            R.id.activity3 -> {
                val intent = Intent(this, RecyclerActivity::class.java)
                this.startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}