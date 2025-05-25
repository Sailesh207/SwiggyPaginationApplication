package com.example.swiggynewapplication.DomainLayer.DataClass

import com.google.gson.annotations.SerializedName

data class MovieResponse (
    @SerializedName("Search") val search : List<MovieData>,
    @SerializedName("totalResults") val totalResults : String,
    @SerializedName("Response") val response : String,
    @SerializedName("Error") val error : String?
)

data class MovieData(
    @SerializedName("imdbID") val id : String,
    @SerializedName("Title") val movieTitle : String,
    @SerializedName("Year") val releaseYear : String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster : String,
)