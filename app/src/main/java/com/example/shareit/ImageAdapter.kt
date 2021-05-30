package com.example.shareit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ImageAdapter (private var images:List<Image>, private val context: Context):
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.image,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = images[position]
        Picasso.get().load(item.imageUrl).into(holder.imageView)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

}