package com.example.instagram.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.instagram.R
import com.example.instagram.model.ContentDTO
import com.example.instagram.util.loadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_grid.view.*

class GridFragment : Fragment(){

    lateinit var mainView :View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView =  inflater.inflate(R.layout.fragment_grid,container,false)
        mainView.gridfragment_recyclerview.adapter = GridFragmentRecyclerviewAdapter()
        mainView.gridfragment_recyclerview.layoutManager = GridLayoutManager(context,3)
        return mainView
    }

    inner class GridFragmentRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var contentDTOs : ArrayList<ContentDTO> = ArrayList()

        init {
            FirebaseFirestore.getInstance().collection("images").orderBy("timestamp",Query.Direction.DESCENDING)//내림차순 정렬
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    for(snapshot in querySnapshot!!.documents){
                        val item = snapshot.toObject(ContentDTO::class.java)
                        contentDTOs.add(item!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val width = resources.displayMetrics.widthPixels / 3 //화면의 가로 픽셀값
            val imageView = ImageView(context)
            imageView.layoutParams = LinearLayoutCompat.LayoutParams(width,width)

            return GridViewHolder(imageView)
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val imageView = (holder as GridViewHolder).imageView
            imageView.loadImage(contentDTOs[position].imageUri!!,context!!)
        }
    }

}

class GridViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)