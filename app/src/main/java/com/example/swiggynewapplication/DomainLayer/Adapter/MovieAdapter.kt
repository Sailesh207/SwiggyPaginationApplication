package com.example.swiggynewapplication.DomainLayer.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swiggynewapplication.DomainLayer.DataClass.MovieData
import com.example.swiggynewapplication.R

class MovieAdapter : ListAdapter<MovieData, MovieAdapter.MovieViewHolder>(MovieDiffUtil()) {

    inner class MovieViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val movieTitle : TextView = view.findViewById(R.id.movie_title)
        val releaseDate : TextView = view.findViewById(R.id.release_date)
        val imageView : ImageView = view.findViewById(R.id.movie_poster)
        fun bind(movieData: MovieData){
            Glide.with(itemView.context)
                .load(movieData.poster)
                .into(imageView)
            movieTitle.text = movieData.movieTitle
            releaseDate.text = movieData.releaseYear
        }
    }

    class MovieDiffUtil : DiffUtil.ItemCallback<MovieData>(){
        override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}