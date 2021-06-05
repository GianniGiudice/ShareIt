package com.example.shareit

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class CustomAdapter(private var images:List<Image>):
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.image,
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = images[position]
        Picasso.get().load(item.imageUrl).into(viewHolder.imageView)
        viewHolder.imageTitleView.text = item.name
        viewHolder.imageDescriptionView.text = item.description

        viewHolder.itemView.setOnClickListener{
            Toast.makeText(it.context, "Click " + item.name, Toast.LENGTH_SHORT).show()

            val intent = Intent(it.context, ImageDisplayActivity::class.java)
            intent.putExtra("EXTRA_IMAGEVIEW_URL", item.imageUrl)
            it.context.startActivity(intent)
        }
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val imageTitleView: TextView = view.findViewById(R.id.imageTitleView)
        val imageDescriptionView: TextView = view.findViewById(R.id.imageDescriptionView)
    }

}