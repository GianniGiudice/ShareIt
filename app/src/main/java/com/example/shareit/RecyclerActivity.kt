package com.example.shareit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shareit.activities.HomeActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.recycler.*


class RecyclerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler)

        //val storageRef = FirebaseStorage.getInstance().getReference("uploads")
        val databaseRef = FirebaseDatabase.getInstance().getReference("uploads")
        val imageList: ArrayList<Image> = ArrayList()

        /*val listAllTask: Task<ListResult> = storageRef.listAll()
        listAllTask.addOnCompleteListener { result ->
            val items: List<StorageReference> = result.result!!.items
            println("======================")
            println(result.result!!.items)
            println("======================")
            items.forEachIndexed { index, item ->
                item.downloadUrl.addOnSuccessListener {
                    Log.d("item", "$it")
                    imageList.add(Image(it.toString()))
                }.addOnCompleteListener {
                    recyclerView.adapter = ImageAdapter(imageList, this)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                }
            }
        }*/

        databaseRef.addListenerForSingleValueEvent(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot!!.children
                    // This returns the correct child count...
                    println("count: "+snapshot.children.count().toString())

                    /*children.forEach {
                        println(it.value.toString())
                        imageList.add(Image(it.child("imageUrl").value.toString(),it.child("name").value.toString(),it.child("description").value.toString()))
                    }*/
                    children.mapNotNullTo(imageList) { it.getValue(Image::class.java) }
                    recyclerView.adapter = CustomAdapter(imageList)
                    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
                    println(imageList.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", error!!.message)
                }

            })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true;
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