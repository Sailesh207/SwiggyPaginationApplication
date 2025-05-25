package com.example.swiggynewapplication.RepositoryLayer.Repository

import android.util.Log
import com.example.swiggynewapplication.DomainLayer.DataClass.MovieResponse
import com.example.swiggynewapplication.RepositoryLayer.NetworkModule.MovieApiService
import io.reactivex.Observable
import javax.inject.Inject

class MovieRepository @Inject constructor(private val apiService: MovieApiService) {


    fun getMoviesByTitle(query : String, page : Int) : Observable<MovieResponse> {
        return try {
            apiService.getMoviesByTitle(title = query, page = page)
        }
        catch (e : Exception){
            Log.e("API Response Failed: ", e.message.toString())
            Observable.error(e)
        }
    }


}