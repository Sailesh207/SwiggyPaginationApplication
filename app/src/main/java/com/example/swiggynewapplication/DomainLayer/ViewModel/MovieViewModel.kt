package com.example.swiggynewapplication.DomainLayer.ViewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swiggynewapplication.DomainLayer.DataClass.MovieData
import com.example.swiggynewapplication.DomainLayer.DataClass.MovieResponse
import com.example.swiggynewapplication.RepositoryLayer.Repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor (private val repository: MovieRepository) : ViewModel() {
    private val movieDetails : MutableLiveData<List<MovieData>> = MutableLiveData()
    var getMovieDetails : LiveData<List<MovieData>> = movieDetails
    var currentQuery = ""
    var currentPage = 1
    var totalPages = 1
    var isLoading = false
    val searchSubject = PublishSubject.create<String>()
    private var disposable : Disposable

    init {
        disposable = searchSubject
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS)
            .switchMap { query ->
                currentQuery = query
                currentPage = 1
                isLoading = true

                repository.getMoviesByTitle(query, currentPage)
                    .doOnError{
                        Log.e("ViewModel", "Error while fetching data")
                        isLoading = false
                    }
                    .doOnTerminate {
                        isLoading = false
                    }
                    .onErrorReturn {
                        Log.e("MovieViewModel", "Error response: ${it.message}")
                        MovieResponse(
                            emptyList(),
                            totalResults = "0",
                            response = "",
                            error = ""
                        )
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .subscribe{ response ->
                if(response.response == "True"){
                    totalPages = (response.totalResults.toIntOrNull() ?: 0) / 10 + 1
                    movieDetails.value = response.search

                }
            }
    }

    @SuppressLint("CheckResult")
    fun fetchMovies(query : String, page : Int) {
        if(isLoading) return

        isLoading = true
        repository.getMoviesByTitle(query, page)
            .doOnError{
                Log.e("ViewModel", "Error while fetching data")
                isLoading = false
            }
            .doOnTerminate {
                isLoading = false
            }
            .onErrorReturn {
                Log.e("MovieViewModel", "Error response: ${it.message}")
                MovieResponse(
                    emptyList(),
                    totalResults = "0",
                    response = "",
                    error = ""
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ response ->
                if(response.response == "True"){
                    totalPages = (response.totalResults.toIntOrNull() ?: 0) / 10 + 1
                    if(page == 1){
                        movieDetails.value = response.search
                    }
                    else{
                        movieDetails.value = movieDetails.value.orEmpty() + response.search
                    }
                }
            }
    }

    fun loadMoreMovies() {
        if(currentPage < totalPages && !isLoading){
            currentPage++
            fetchMovies(currentQuery, currentPage)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}