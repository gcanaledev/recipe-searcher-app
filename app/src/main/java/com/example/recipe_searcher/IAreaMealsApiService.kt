package com.example.recipe_searcher

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

val HTTPCallImplementation = Retrofit.Builder()
                                             .baseUrl("www.themealdb.com/api/json/v1/1/")
                                             .addConverterFactory(GsonConverterFactory.create())
                                             .build()
                                             .create(IAreaMealsApiService::class.java)

interface IAreaMealsApiService{

    @GET("filter.php")
    suspend fun getResponseObject(): AreaMeals
}