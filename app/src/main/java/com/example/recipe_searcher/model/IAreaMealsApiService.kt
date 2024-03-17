package com.example.recipe_searcher.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

fun getHttpRequestImplementation(requestedArea: Area): IAreaMealsApiService{
    return Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/filter.php?a=${requestedArea}")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IAreaMealsApiService::class.java)

}

interface IAreaMealsApiService{

    @GET
    suspend fun getResponseObject(): AreaMeals
}