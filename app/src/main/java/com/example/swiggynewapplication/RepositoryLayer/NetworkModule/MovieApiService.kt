package com.example.swiggynewapplication.RepositoryLayer.NetworkModule

import com.example.swiggynewapplication.DomainLayer.DataClass.MovieResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

//http://www.omdbapi.com/apikey.aspx

interface MovieApiService {
    @GET("?")
    fun getMoviesByTitle(
        @Query("apikey") apikey : String = "1c9aafa5",
        @Query("s") title : String,
        @Query("type") type : String = "movie",
        @Query("page") page : Int = 1,
        @Query("limit") limit : Int = 10
    ) : Observable<MovieResponse>

    companion object{
        const val BASE_URL = "https://www.omdbapi.com/"
    }
}