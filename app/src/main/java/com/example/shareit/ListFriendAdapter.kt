package com.example.shareit

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


class ListFriendAdapter(val list: List<String>, val url:String, val title:String, val description:String) : RecyclerView.Adapter<ListFriendAdapter.ViewHolder>() {
    private lateinit var mStorageRef: StorageReference;
    private lateinit var mDatabaseRef: DatabaseReference;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFriendAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListFriendAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(string: String) {
            itemView.findViewById<TextView>(R.id.friendTextView).text = string
            itemView.setOnClickListener {
                uploadFile(list[position])
                Toast.makeText(it.context, "L'image "+title+" a été envoyé à "+list[position], Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun uploadFile(string: String){

        Picasso.get().load(url).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                val baos = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data: ByteArray = baos.toByteArray()
                mDatabaseRef = FirebaseDatabase.getInstance().getReference(string)
                mStorageRef = FirebaseStorage.getInstance().getReference("uploads/"+string)
                val fileReference = mStorageRef.child(System.currentTimeMillis()
                    .toString() + ".jpeg")
                var mUploadTask: UploadTask = fileReference.putBytes(data)
                mUploadTask.addOnSuccessListener { taskSnapshot ->
                    println("Upload $url")
                    if (url != null) {
                        val upload = Upload(title.trim { it <= ' ' },
                            url,
                            description.trim { it <= ' ' })
                        val key = mDatabaseRef.push().key
                        println("uploadId $key")
                        if (key != null) {
                            mDatabaseRef.child(key).setValue(upload)
                        }
                    }
                }
            }
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
        })
    }
}
