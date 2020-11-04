package com.example.mydailynews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyNewsAdapter(private val listener: NewsItemClicked) :
    RecyclerView.Adapter<NewsViewHolder>() {

    private val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        /* Converting our xml item element into view using LayoutInflater */
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        val viewHolder = NewsViewHolder(view)

        /* Handling click events */
        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        /* fetching current item */
        val currentItem = items[position]
        //  Binding the data
        holder.titleView.text = currentItem.title
        holder.author.text = currentItem.author
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.imageNews)
    }

    /*  To Update the News */
    fun upDateNews(updatedNews: ArrayList<News>) {
        items.clear()
        items.addAll(updatedNews)

        notifyDataSetChanged()
    }
}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.tv_title)
    val imageNews: ImageView = itemView.findViewById(R.id.iv_news)
    val author: TextView  =itemView.findViewById(R.id.tv_author)
}

/* creating a click callback using interface to notify our activity */
interface NewsItemClicked {
    fun onItemClicked(items: News)
}