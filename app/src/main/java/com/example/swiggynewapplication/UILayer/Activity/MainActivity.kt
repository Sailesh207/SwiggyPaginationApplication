package com.example.swiggynewapplication.UILayer.Activity

import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.swiggynewapplication.DomainLayer.Adapter.MovieAdapter
import com.example.swiggynewapplication.DomainLayer.ViewModel.MovieViewModel
import com.example.swiggynewapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.movieList
        val adapter = MovieAdapter()
        recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.isNotEmpty() == true){
                    viewModel.searchSubject.onNext(newText)
                }
                return true
            }
        })

        viewModel.getMovieDetails.observe(this){ movie ->
            adapter.submitList(movie)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastItemPosition = layoutManager.findLastVisibleItemPosition()

                if(lastItemPosition + 2 >= totalItemCount && !viewModel.isLoading){
                    viewModel.loadMoreMovies()
                }
            }
        })
    }
}