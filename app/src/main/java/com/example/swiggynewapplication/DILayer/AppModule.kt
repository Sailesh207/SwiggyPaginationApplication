package com.example.swiggynewapplication.DILayer

import com.example.swiggynewapplication.RepositoryLayer.NetworkModule.MovieApiService
import com.example.swiggynewapplication.RepositoryLayer.Repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Provides
    @Singleton
    fun getRepository(apiService: MovieApiService) : MovieRepository{
        return MovieRepository(apiService)
    }

    @Provides
    @Singleton
    fun getRetrofitInstance() : MovieApiService{
        return Retrofit.Builder()
            .baseUrl(MovieApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }

}