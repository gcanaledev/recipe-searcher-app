package com.example.recipe_searcher.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

fun getHttpRequestImplementation(): IAreaMealsApiService{
    return Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IAreaMealsApiService::class.java)

}

interface IAreaMealsApiService{

    @GET("filter.php")
    suspend fun getResponseObject(@Query("a") a: String): AreaMeals

}