package com.example.filters.ImageFilter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filters.R
import com.zomato.photofilters.imageprocessors.Filter

class FilterAdapter( private val data : List<ThumbnailItem>,private val imageUri: Uri,private val listener:ThumnailClickListener) :RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    inner class FilterViewHolder(itemview: View):RecyclerView.ViewHolder(itemview)
    {

             val filterImg = itemview.findViewById<ImageView>(R.id.FilterImg)
             val filter_name = itemview.findViewById<TextView>(R.id.filtername)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val viewHolder = FilterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.filter_rec,parent,false))


        viewHolder.itemView.setOnClickListener {


            listener.onThumnailClickListener(data[viewHolder.absoluteAdapterPosition].filter)

        }


            return viewHolder
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {


          val filter = data[position]
        holder.filter_name.text =  filter.filter.name
        imageUri.let {
            Glide.with(holder.itemView.context).load(filter.image).into(holder.filterImg)
        }













    }

    override fun getItemCount(): Int {

          return data.size


    }


    interface ThumnailClickListener
    {

        fun onThumnailClickListener(filter: Filter)

    }


}